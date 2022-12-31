package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    HikariDataSource dataSource;
    Connection connection;
    public String id;

    public User(String id) {
        this.id = id;

        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public float getUserBalance() {
        try (PreparedStatement statement = connection.prepareStatement("""
            SELECT cash from userport WHERE id = ?
            """)) {
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) return resultSet.getFloat(1);
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0F;
    }

    public float getUserPortfolioValue() {

        float currentValue = 0;

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
            } catch (SQLException | IOException | JSONException e) {
                throw new RuntimeException(e);
            }
        return currentValue;
    }

    public ResultSet getUserStocks() {


        try(PreparedStatement statement = connection.prepareStatement("""
            SELECT stockticker, shares, orderdate, buyPrice from usersportfolio WHERE id = ?
            """)) {
                statement.setString(1, id);
                return statement.executeQuery();
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public float getAmountSpent() {

        float totalAmountSpent = 0;

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
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalAmountSpent;
    }

    public boolean checkCash(float amountSpent) throws SQLException {
        float currentCash = getUserBalance();
        return !(amountSpent > currentCash);
    }

    public void updatePortfolioValues(String orderType, float amountSpent) throws SQLException {

        float currentCash = 0;
        float currentPortfolioValue = getUserPortfolioValue();


        try(PreparedStatement statement = connection.prepareStatement("""
            SELECT cash from userport WHERE id = ?
            """)) {
                statement.setString(1, id);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()) {
                    currentCash = resultSet.getFloat(1);
                }
            }

        if (orderType.equals("buy")) {
            currentCash -= amountSpent;
        } else {
            currentCash += amountSpent;
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
