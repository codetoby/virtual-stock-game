package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdatePortfolio {

    HikariDataSource dataSource;

    public String id;
    public String orderType;

    public UpdatePortfolio(String id, String orderType) {
        this.id = id;
        this.orderType = orderType;

        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getUserInfo() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
            SELECT cash, portfolio from userport WHERE id = ?
            """)) {
                statement.setString(1, id);
                return statement.executeQuery();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public float portfolioValue() {

        float currentValue = 0;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
            SELECT stockticker, shares from usersportfolio WHERE id = ?
            """)) {
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    JSONObject stockInfo = new StockTicker(resultSet.getString(1)).stockInfo();
                    float stockPrice = stockInfo.getInt("c");
                    currentValue += stockPrice * resultSet.getInt(2);
                }
            }
        } catch (SQLException | IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        return currentValue;
    }

    public ResultSet userStocks() {
        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("""
                SELECT stockticker, shares, orderdate, buyPrice from usersportfolio WHERE id = ?
        """)) {
                statement.setString(1, id);
                return statement.executeQuery();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public float totalSpent() {

        float totalAmountSpent = 0;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
            SELECT buyPrice, shares from usersportfolio WHERE id = ?
            """)) {
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    float buyPrice = resultSet.getFloat(1);
                    int shares = resultSet.getInt(2);
                    totalAmountSpent += (buyPrice * shares);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return totalAmountSpent;
    }

    public boolean checkCash(float amountSpent) throws SQLException {

        float currentCash = 0;
        ResultSet resultSet = getUserInfo();
        while(resultSet.next()) {
            currentCash = resultSet.getInt(1);
        }
        return !(amountSpent > currentCash);
    }

    public void updatePortfolioValues(float amountSpent) throws SQLException {

        float currentCash = 0;
        float currentPortfolioValue = 0;

        try (Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement("""
            SELECT cash, portfolio from userport WHERE id = ?
            """)) {
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    currentCash = resultSet.getFloat(1);
                    currentPortfolioValue = resultSet.getFloat(2);
                }
            }

            if (orderType.equals("buy")) {
                currentCash -= amountSpent;
                currentPortfolioValue += amountSpent;
            } else {
                currentCash += amountSpent;
                currentPortfolioValue -= amountSpent;
            }

            try(PreparedStatement statement = connection.prepareStatement("""
                UPDATE userport SET cash = ?, portfolio = ? WHERE id = ?
                """)) {
                statement.setFloat(1, currentCash);
                statement.setFloat(2, currentPortfolioValue);
                statement.setString(3, id);
                statement.executeQuery();
            }
        }
    }
}
