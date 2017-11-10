package com.epam.training.jf.db.cp;

import com.epam.training.jf.db.model.Person;
import org.junit.jupiter.api.Test;

class ConnectionPoolTest {

    //language=H2
    private static final String SQL =
            "SELECT id, first_name, last_name, permission, dob, email, password, address, telephone FROM Person";

    private JdbcConnectionPool connectionPool =
            new ConnectionPool("/jdbc.properties", "/h2.sql");

    @Test
    void name() {
        connectionPool.withResultSet(rs -> {
            while (rs.next())
                System.out.println(Person.from(rs));
        }, SQL);
    }
}