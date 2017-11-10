package com.epam.training.jf.db.model;

import java.sql.ResultSet;
import java.time.LocalDate;

public interface Person {

    static Person from(ResultSet resultSet) {
        return SimplePerson.from(resultSet);
    }

    int getId();

    String getFirstName();

    String getLastName();

    boolean isPermission();

    LocalDate getDob();

    String getEmail();

    String getPassword();

    String getAddress();

    String getTelephone();
}
