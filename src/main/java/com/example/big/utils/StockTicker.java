package com.example.big.utils;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;


public class StockTicker {

    public JSONObject stockInfo(String stockTicker) throws IOException {

        String Url = "https://finnhub.io/api/v1/quote?token=cbobmcaad3i6ndrm5uag&symbol=" + stockTicker.toUpperCase();

        URL url = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        InputStream responseStream = connection.getInputStream();
        String result = new BufferedReader(new InputStreamReader(responseStream)).lines().collect(Collectors.joining("\n"));

        JSONObject jsonObject = new JSONObject(result);
        return jsonObject;
    }
}
