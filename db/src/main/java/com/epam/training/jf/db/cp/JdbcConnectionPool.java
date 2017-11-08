package com.epam.training.jf.db.cp;

import lombok.SneakyThrows;
import lombok.val;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface JdbcConnectionPool extends Supplier<Connection> {

    @SneakyThrows
    default <T> T mapConnection(Function<Connection, T> connectionMapper) {
        try (val connection = get()) {
            return connectionMapper.apply(connection);
        }
    }

    @SneakyThrows
    default void withConnection(Consumer<Connection> connectionConsumer) {
        try (val connection = get()) {
            connectionConsumer.accept(connection);
        }
    }

    default <T> T mapStatement(Function<Statement, T> statementMapper) {
        return mapConnection(connection -> {
            try (val statement = connection.createStatement()) {
                return statementMapper.apply(statement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }



    default void withStatement(Consumer<Statement> statementConsumer) {
        withConnection(connection -> {
            try (val statement = connection.createStatement()) {
                statementConsumer.accept(statement);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
