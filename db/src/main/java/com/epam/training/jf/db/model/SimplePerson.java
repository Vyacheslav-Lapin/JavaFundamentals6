package com.epam.training.jf.db.model;

import lombok.SneakyThrows;
import lombok.Value;

import java.sql.ResultSet;
import java.time.LocalDate;

@Value
public class SimplePerson implements Person {
    private int id;
    private String firstName;
    private String lastName;
    private boolean permission;
    private LocalDate dob;
    private String email;
    private String password;
    private String address;
    private String telephone;

    @SneakyThrows
    public static SimplePerson from(ResultSet resultSet) {
        return new SimplePerson(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getBoolean("permission"),
                resultSet.getDate("dob").toLocalDate(),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("address"),
                resultSet.getString("telephone")
        );
    }

}
