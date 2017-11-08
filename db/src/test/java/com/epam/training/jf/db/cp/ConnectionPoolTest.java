package com.epam.training.jf.db.cp;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ConnectionPoolTest {

    public static final String SQL =
            "SELECT id, first_name, last_name, permission, dob, email, password, address, telephone FROM Person";

    Supplier<Connection> connectionPool =
            new ConnectionPool("/jdbc.properties");

    @Test
    @SneakyThrows
    void name() {
        try (Connection con = connectionPool.get();
             Statement st = con.createStatement()) {

            st.execute(getInitSql());

            try (ResultSet rs = st.executeQuery(SQL)) {
                while (rs.next()) {
                    System.out.printf("%d %s %s%n",
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"));
                }
            }
        }
    }

    @SneakyThrows
    private static String getInitSql() {
        try (Stream<String> lines = Files.lines(
                Paths.get("./src/main/resources/h2.sql"),
                StandardCharsets.UTF_8)) {
            return lines.collect(Collectors.joining());
        }
    }
}