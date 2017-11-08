package com.epam.training.jf.db.cp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPoolTest {

    ConnectionPool connectionPool =
            new ConnectionPool("/jdbc.properties");

    @Test
    void name() {
//        connectionPool
    }
}