package com.example.big.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WebPortfolio {

    public String id;
    User user;

    public WebPortfolio(String id) {
        this.id = id;
        user = new User(id);
    }

    public JSONArray webPortfolio() throws SQLException, IOException {

        JSONArray userPortfolio = new JSONArray();
        ResultSet resultSet =  user.getUserStocks();

        while(resultSet.next()) {

            JSONObject stockEntry = new JSONObject();

            String stockTicker = resultSet.getString(1);
            int shares = resultSet.getInt(2);
            Date buyDate = resultSet.getDate(3);
            float buyPrice = resultSet.getFloat(4);

            JSONObject stockInfo = new StockTicker(stockTicker).stockInfo();
            float stockPrice = stockInfo.getInt("c");
            float todayChange = stockInfo.get("d") != null ? stockInfo.getFloat("d") : 0F;
            float todayPerChange = stockInfo.get("dp") != null ? stockInfo.getFloat("dp") : 0F;
            float prevDayClose = stockInfo.get("pc") != null ? stockInfo.getFloat("pc") : 0F;

            float buyAmount = buyPrice * shares;
            float currentAmount = stockPrice * shares;

            float profit = currentAmount - buyAmount;
            float profitPer = ((currentAmount / buyAmount) - 1) * 100;

            stockEntry.put("stockTicker", stockTicker);
            stockEntry.put("shares", shares);
            stockEntry.put("buyDate", buyDate);
            stockEntry.put("stockPrice", stockPrice);
            stockEntry.put("todayChange", todayChange);
            stockEntry.put("todayPerChange", todayPerChange);
            stockEntry.put("prevDayClose", prevDayClose);
            stockEntry.put("buyAmount", buyAmount);
            stockEntry.put("currentAmount", currentAmount);
            stockEntry.put("profit", profit);
            stockEntry.put("profitPer", profitPer);

            userPortfolio.put(stockEntry);
        }

        return userPortfolio;
    }

}
