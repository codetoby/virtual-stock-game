package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class UserHistory {

    String stockTicker;
    String userID;
    HikariDataSource dataSource;
    UserHistory(String userID, String stockTicker) {

        try {
            this.dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.userID = userID;
        this.stockTicker = stockTicker;
    }

    public void insertData(int shares, String orderType, Date entryDate) {

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("""
                INSERT INTO userhistory (stockTicker, shares, orderType, entryDate)
                VALUES (?, ?, ?, ?)
            """)) {
                statement.setString(1, userID);
                statement.setString(2, this.stockTicker);
                statement.setInt(3, shares);
                statement.setString(4, orderType);
                statement.setDate(5, entryDate);
                statement.executeQuery();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray getUserHistory() {

        JSONArray stocks = new JSONArray();

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("""
                SELECT * from userhistory WHERE id = ? and  stockTicker = ?
            """)) {
                statement.setString(1, userID);
                statement.setString(2, stockTicker);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    JSONObject stock = new JSONObject();
                    stock.put("stockTicker", resultSet.getString(1));
                    stock.put("shares", resultSet.getInt(2));
                    stock.put("orderType", resultSet.getString(3));
                    stock.put("entryDate", resultSet.getString(4));
                    stock.put("buyPrice", resultSet.getFloat(5));
                    stocks.put(stock);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }


}

