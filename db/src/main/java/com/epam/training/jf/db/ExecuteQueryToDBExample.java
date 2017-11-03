package com.epam.training.jf.db;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExecuteQueryToDBExample {

    public static final String SQL =
            "SELECT id, first_name, last_name, permission, dob, email, password, address, telephone FROM Person";

    @SuppressWarnings("unchecked")
    private static Properties properties = new Properties() {
        public Properties load(String path) {
            try (InputStream resourceAsStream =
                         getClass().getResourceAsStream(path)) {
                load(resourceAsStream);
            } catch (IOException ignored) {
                // TODO: 03/11/2017 log
            }
            return this;
        }
    }.load("/jdbc.properties");

    @SneakyThrows
    public static void main(String... args) {

        // Get and remove values
        Class.forName((String) properties.remove("driver"));
        String url = (String) properties.remove("url");

        assert properties.size() >= 2;
        assert properties.containsKey("user");
        assert properties.containsKey("password");

        try (Connection con = DriverManager.getConnection(url, properties);
             Statement st = con.createStatement()) {

            st.execute(getInitSql());

            try (ResultSet rs = st.executeQuery(SQL)) {
                while (rs.next()) {
                    System.out.printf("%d %s%s%n",
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
                Paths.get("./db/src/main/resources/h2.sql"),
                StandardCharsets.UTF_8)) {
            return lines.collect(Collectors.joining());
        }
    }
}
