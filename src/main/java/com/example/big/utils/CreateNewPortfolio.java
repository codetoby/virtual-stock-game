package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CreateNewPortfolio {

    HikariDataSource dataSource;

    public CreateNewPortfolio(String id) throws SQLException{

        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
            INSERT INTO userport (id) VALUES(?)
            """)) {
                statement.setString(1, id);
                statement.executeQuery();
            }
        }
    }

}
