package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserHistory {

    String stockTicker;
    int shares;
    String orderType;
    Date entryDate;

    UserHistory(String stockTicker, int shares, String orderType, Date entryDate) {
        this.stockTicker = stockTicker;
        this.shares = shares;
        this.orderType = orderType;
        this.entryDate = entryDate;
    }

    public void insertData() {

        HikariDataSource dataSource;

        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("""
                INSERT INTO userhistory (stockTicker, shares, orderType, entryDate)
                VALUES (?, ?, ?, ?)
            """)) {
                statement.setString(1, this.stockTicker);
                statement.setInt(2, this.shares);
                statement.setString(3, this.orderType);
                statement.setDate(4, this.entryDate);
                statement.executeQuery();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

