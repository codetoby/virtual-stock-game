package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Portfolio {

    String id;
    HikariDataSource dataSource;
    public Portfolio(String id) {

        this.id = id;

        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void createPortfolio() throws SQLException {
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
