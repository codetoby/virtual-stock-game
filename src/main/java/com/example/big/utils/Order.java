package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

public class Order {

    // private static final Logger logger = Logger.getLogger(Order.class);

    public static String id;
    public static String stockTicker;
    public static String orderType;
    public static int shares;
    public static float totalAmount;

    public Order(String Userid, String UserStockTicker, String UserOrderType, int UserShares) {
        id = Userid;
        stockTicker = UserStockTicker;
        orderType = UserOrderType;
        shares = UserShares;
    }

    public String newOrder() {

        // date in sql format
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);

        // connect to database
        HikariDataSource dataSource;
        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // check if user has enough cash to buy this stock
        try (Connection connection = dataSource.getConnection()) {
            boolean findStock = false;
            // check if the user already has the stock in his portfolio
            try (PreparedStatement statement = connection.prepareStatement("""
                SELECT COUNT (stockticker) from usersportfolio WHERE id = ? AND stockticker = ?
            """)) {
                statement.setString(1, id);
                statement.setString(2, stockTicker);
                statement.setString(3, orderType);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int stockCount = resultSet.getInt(1);
                    if (stockCount == 1) findStock = true;
                }
            }

            // get the current stock price
            JSONObject stockInfo = new StockTicker().stockInfo(stockTicker);
            float stockPrice = stockInfo.getFloat("c");

            // check if stock ticker is valid
            if (stockInfo.get("d") == null) return "Not a Valid Stock Ticker";

            totalAmount = stockPrice * shares;

            // check if user has enough cash to buy the stock
            new UpdatePortfolio(id, orderType);
            boolean checkCash = UpdatePortfolio.checkCash(totalAmount);

            if (!checkCash) return "You do not have enough cash";

            // if user has bought the stock already, and it is still in the portfolio
            if (findStock) {

                int newShares = 0;
                float averagePrice = 0;

                // orderType -> buy
                // get current info over his position on the stock
                try (PreparedStatement statement = connection.prepareStatement("""
                       SELECT shares, buyprice from usersportfolio WHERE id = ? AND stockticker = ?
                   """)) {
                    statement.setString(1, id);
                    statement.setString(2, stockTicker);
                    statement.setString(3, orderType);
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        if (orderType.equals("buy")) {
                            newShares += (resultSet.getInt(1) + shares);
                            averagePrice = (stockPrice * shares + resultSet.getFloat(2) * resultSet.getInt(1)) / resultSet.getInt(1) + shares;
                        } else {
                            newShares += (resultSet.getInt(1) - shares);
                            averagePrice = (resultSet.getFloat(2) * resultSet.getInt(1) - stockPrice * shares) / newShares;
                        }
                    }
                }
                if (newShares < 0) return "You do not have so many shares";

                // orderType -> sell
                // delete entry if shares are 0
                if (orderType.equals("sell") && newShares == 0) {
                    try (PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM usersportfolio WHERE id = ? AND stockticker = ?
                    """)) {
                        statement.setString(1, id);
                        statement.setString(2, stockTicker);
                        statement.executeQuery();
                    }
                } else {
                    // update the stock entry
                    try (PreparedStatement statement = connection.prepareStatement("""
                    UPDATE usersportfolio SET shares = ?, buyPrice = ? WHERE id = ?
                    """)) {
                        statement.setInt(1, newShares);
                        statement.setFloat(2, averagePrice);
                        statement.setString(3, id);
                        statement.executeQuery();
                    }
                }
            } else if (orderType.equals("buy")) {
                // if the user has not the stock in their portfolio -> new entry
                try (PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO usersportfolio
                    (id, stockticker, shares, orderdate, ordertype, buyPrice)
                    VALUES(?, ?, ?, ?, ?, ?)
                    """)) {
                    statement.setString(1, id);
                    statement.setString(2, stockTicker);
                    statement.setInt(3, shares);
                    statement.setDate(4, date);
                    statement.setString(5, orderType);
                    statement.setFloat(6, stockPrice);
                    statement.executeQuery();
                }
            } else return "You do not have the stock to sell";

        } catch (SQLException | IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        new UpdatePortfolio(id, orderType);
        try {
            UpdatePortfolio.updatePortfolioValues(totalAmount);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        UserHistory insertUserEntry = new UserHistory(id, stockTicker);
        insertUserEntry.insertData(shares, orderType, date);
        return "Success";
    }
}
