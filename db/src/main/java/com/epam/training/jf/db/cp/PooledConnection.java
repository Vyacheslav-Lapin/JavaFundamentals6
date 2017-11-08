package com.epam.training.jf.db.cp;

import lombok.SneakyThrows;
import lombok.experimental.Delegate;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public class PooledConnection implements Connection {

    @Delegate(excludes = Closeable.class)
    private Connection connection;

    private Consumer<PooledConnection> onClose;

    @SneakyThrows
    PooledConnection(Connection connection,
                     Consumer<PooledConnection> onClose) {
        this.connection = connection;
        this.onClose = onClose;
        connection.setAutoCommit(true);
    }

    @SneakyThrows
    public void reallyClose() {
        connection.close();
    }

    @Override
    public void close() throws SQLException {
        if (connection.isClosed())
            throw new SQLException("Attempting to close closed connection.");

        if (connection.isReadOnly())
            connection.setReadOnly(false);

        onClose.accept(this);
    }
}
