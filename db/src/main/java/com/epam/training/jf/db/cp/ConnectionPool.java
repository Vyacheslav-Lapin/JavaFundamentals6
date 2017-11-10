package com.epam.training.jf.db.cp;

import com.epam.training.jf.db.common.ResourceProperties;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedFunction2;
import io.vavr.Function1;
import io.vavr.Function2;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Log4j2
public class ConnectionPool implements JdbcConnectionPool, Closeable {

    private static final int DEFAULT_POOL_SIZE = 5;

    private static final Function<String, Class<?>> loadClass =
            ((CheckedFunction1<String, Class<?>>) Class::forName).unchecked();

    private static final Function2<String, Properties, Connection> connectionFromUrlAndProps =
            ((CheckedFunction2<String, Properties, Connection>) DriverManager::getConnection).unchecked();

    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    private BlockingQueue<PooledConnection> connectionQueue;

    private volatile boolean isClosed;

    private final Consumer<PooledConnection> onClose = pooledConnection -> {
        if (isClosed)
            pooledConnection.reallyClose();
        else
            connectionQueue.offer(pooledConnection);
    };

    public ConnectionPool(String jdbcPropertiesName, String resourceName) {
        this(jdbcPropertiesName);
        executeSql(resourceName);
    }

    @SuppressWarnings("WeakerAccess")
    public ConnectionPool(String jdbcPropertiesName) {

        val resourceProperties = new ResourceProperties(jdbcPropertiesName);

        //noinspection ResultOfMethodCallIgnored
        resourceProperties.getAndRemove("driver")
                .map(loadClass)
                .orElseThrow(() -> new ConnectionPoolException("SQLException in ConnectionPool"));

        int poolSize = resourceProperties.getAndRemove("poolSize")
                .map(Integer::parseInt)
                .orElse(DEFAULT_POOL_SIZE);

        Function1<Properties, Connection> connectionFromProps =
                resourceProperties.getAndRemove("url")
                        .map(connectionFromUrlAndProps::apply)
                        .orElseThrow(() -> new ConnectionPoolException("URL is not defined"));

        assert resourceProperties.containsKeysOnly("user", "password");

        val properties = resourceProperties.getProperties();
        Supplier<Connection> connectionSupplier = () -> connectionFromProps.apply(properties);

        connectionQueue = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++)
            connectionQueue.add(new PooledConnection(connectionSupplier.get(), onClose));
    }

    @Override
    public Connection get() throws ConnectionPoolException {
        try {
            return connectionQueue.take();
        } catch (InterruptedException e) {
            throw new ConnectionPoolException("Error connecting to the data source.", e);
        }
    }

    @Override
    public void close() {
        isClosed = true;
        for (Iterator<PooledConnection> iterator = connectionQueue.iterator(); iterator.hasNext(); ) {
            iterator.next().reallyClose();
            iterator.remove();
        }
    }
}
