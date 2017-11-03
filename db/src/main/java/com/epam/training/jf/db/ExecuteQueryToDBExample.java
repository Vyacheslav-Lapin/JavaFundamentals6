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

    private static Properties properties = new Properties() {
        public Properties load(String path) {
            try (InputStream resourceAsStream =
                         ExecuteQueryToDBExample.class.getResourceAsStream(path)) {
                load(resourceAsStream);
            } catch (IOException ignored) {
                // TODO: 03/11/2017 log
            }
            return this;
        }
    }.load("/jdbc.properties");

    public static void main(String... args) throws ClassNotFoundException, IOException, SQLException {

        String driver = properties.getProperty("driver", "org.gjt.mm.mysql.Driver");
        Class.forName(driver);

        String initSql = getInitSql();

        String url = properties.getProperty("url", "jdbc:mysql://127.0.0.1/test");
        String login = properties.getProperty("user", "root");
        String password = properties.getProperty("password");
        try (Connection con = DriverManager.getConnection(url, login, password);
             Statement st = con.createStatement()) {

            st.execute(initSql);

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
