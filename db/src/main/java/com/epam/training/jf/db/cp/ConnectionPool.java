package com.epam.training.jf.db.cp;

import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@Log4j2
public class ConnectionPool implements Supplier<Connection>, Closeable {

    private static final int DEFAULT_POOL_SIZE = 5;

    private BlockingQueue<PooledConnection> connectionQueue;

    private boolean isClosed;

    public ConnectionPool(String jdbcPropertiesName) {

        val properties = new Properties() {
            public Properties load(String path) {
                try (InputStream resourceAsStream =
                             getClass().getResourceAsStream(path)) {
                    load(resourceAsStream);
                } catch (IOException ignored) {
                    log.warn("Resource is unaccessible!");
                }
                return this;
            }
        }.load(jdbcPropertiesName);

        try {
            // Get and remove values
            Class.forName((String) properties.remove("driver"));
            String url = (String) properties.remove("url");

            int poolSize = ofNullable(
                    (String) properties.remove("poolSize"))
                    .map(Integer::parseInt)
                    .orElse(DEFAULT_POOL_SIZE);

            Locale.setDefault(Locale.ENGLISH);

            assert properties.size() >= 2;
            assert properties.containsKey("user");
            assert properties.containsKey("password");

            connectionQueue = new ArrayBlockingQueue<>(poolSize);

            for (int i = 0; i < poolSize; i++)
                connectionQueue.add(
                        new PooledConnection(
                                DriverManager.getConnection(url, properties),
                                pooledConnection -> {
                                    if (isClosed)
                                        pooledConnection.reallyClose();
                                    else
                                        connectionQueue.offer(pooledConnection);
                                }));

        } catch (SQLException e) {
            throw new ConnectionPoolException("SQLException in ConnectionPool", e);
        } catch (ClassNotFoundException e) {
            throw new ConnectionPoolException("Can't find database driver class", e);
        }
    }

    @Override
    public Connection get() throws ConnectionPoolException {
        try {
            return connectionQueue.take();
        } catch (InterruptedException e) {
            throw new ConnectionPoolException(
                    "Error connecting to the data source.", e);
        }
    }

    @Override
    public void close() {
        isClosed = true;
        for (Iterator<PooledConnection> iterator =
             connectionQueue.iterator(); iterator.hasNext(); ) {
                iterator.next().reallyClose();
                iterator.remove();
        }
    }
}
