package com.epam.training.jf.db.cp;

import com.epam.training.jf.db.common.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.Function1;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.nio.charset.StandardCharsets.UTF_8;

@FunctionalInterface
public interface JdbcConnectionPool extends Supplier<Connection> {

    @SneakyThrows
    default <T> T mapConnection(CheckedFunction1<Connection, T> connectionMapper) {
        Function<Connection, T> mapper = connectionMapper.unchecked();
        try (val connection = get()) {
            return mapper.apply(connection);
        }
    }

    @SneakyThrows
    default void withConnection(CheckedConsumer<Connection> connectionConsumer) {
        Consumer<Connection> consumer = connectionConsumer.unchecked();
        try (val connection = get()) {
            consumer.accept(connection);
        }
    }

    default <T> T mapStatement(CheckedFunction1<Statement, T> statementMapper) {
        Function<Statement, T> mapper = statementMapper.unchecked();
        return mapConnection(connection -> {
            try (val statement = connection.createStatement()) {
                return mapper.apply(statement);
            }
        });
    }

    default void withStatement(CheckedConsumer<Statement> statementConsumer) {
        Consumer<Statement> consumer = statementConsumer.unchecked();
        withConnection(connection -> {
            try (val statement = connection.createStatement()) {
                consumer.accept(statement);
            }
        });
    }

    @SneakyThrows
    private String getInitSql(String name) {

        val scanner = new Scanner(getClass().getResourceAsStream(name))
                .useDelimiter(System.lineSeparator());

        Stream<String> lines = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(scanner, Spliterator.ORDERED), false);

        try (scanner; lines) {
            return lines.collect(Collectors.joining());
        }
    }

    default void executeSql(String resourceName) {
        String sql = getInitSql(resourceName);
        withStatement(statement -> statement.execute(sql));
    }

    default <T> T mapPreparedStatement(CheckedFunction1<PreparedStatement, T> preparedStatementMapper,
                                       String sql,
                                       Object... params) {

        Function1<PreparedStatement, T> mapper = preparedStatementMapper.unchecked();

        return mapConnection(connection -> {
            val preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < params.length;) {
                Object param = params[i];
                preparedStatement.setObject(++i, param);
            }

            try (preparedStatement) {
               return mapper.apply(preparedStatement);
            }
        });
    }

    default void withPreparedStatement(CheckedConsumer<PreparedStatement> preparedStatementConsumer,
                                       String sql,
                                       Object... params) {

        Consumer<PreparedStatement> consumer = preparedStatementConsumer.unchecked();

        withConnection(connection -> {
            val preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < params.length;) {
                Object param = params[i++];
                preparedStatement.setObject(i, param);
            }

            try (preparedStatement) {
                consumer.accept(preparedStatement);
            }
        });
    }

    default <T> T mapResultSet(CheckedFunction1<ResultSet, T> resultSetMapper, String sql) {
        Function<ResultSet, T> mapper = resultSetMapper.unchecked();
        return mapStatement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            try (resultSet) {
                return mapper.apply(resultSet);
            }
        });
    }

    default void withResultSet(CheckedConsumer<ResultSet> connectionConsumer,
                               String sql) {
        withStatement(statement -> {
            ResultSet resultSet = statement.executeQuery(sql);
            try (resultSet) {
                connectionConsumer.unchecked().accept(resultSet);
            }
        });
    }

    default <T> T mapResultSet(CheckedFunction1<ResultSet, T> resultSetMapper,
                               String sql,
                               Object... params) {
        Function<ResultSet, T> mapper = resultSetMapper.unchecked();
        return mapPreparedStatement(preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            try (resultSet) {
                return mapper.apply(resultSet);
            }
        }, sql, params);
    }

    default void withResultSet(CheckedConsumer<ResultSet> resultSetConsumer,
                               String sql,
                               Object... params) {
        Consumer<ResultSet> mapper = resultSetConsumer.unchecked();
        withPreparedStatement(preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            try (resultSet) {
                mapper.accept(resultSet);
            }
        }, sql, params);
    }
}
