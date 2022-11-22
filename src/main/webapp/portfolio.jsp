<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<html>
<head>
    <title>Portfolio</title>
</head>
<body>

    <ul>
        <li><a href="/portfolio">portfolio</a></li>
        <li><a href="/order">order</a></li>
        <li><a href="/register">register</a></li>
        <li><a href="/login">login</a></li>
        <li>
            <form action="/stockticker" method="get">
                <label for="ticker">Search Stock Ticker</label>
                <input type="search" id="ticker" name="ticker">
                <input type="submit">
            </form>
        </li>
    </ul>

    <h1>Portfolio</h1>
    <h3>cash ${userinfo.getFloat("cash")}</h3>
    <h3>portfolio ${userinfo.getFloat("portfolio")}</h3>
    <h3>Profit Loss ${userinfo.getFloat("pl")}</h3>
    <h3>Change Total % ${userinfo.getFloat("ct")}</h3>

    <%
        JSONArray webPortfolio = (JSONArray) request.getAttribute("webPortfolio");
    %>
    <table border="1">
        <tr>
            <td>stock ticker</td>
            <td>shares</td>
            <td>stockPrice</td>
            <td>profit</td>
            <td>buyAmount</td>
        </tr>
        <%for (int i = 0; i < webPortfolio.length(); i++){ %>
        <tr>
            <% JSONObject object = webPortfolio.getJSONObject(i); %>
            <td> <%= object.getString("stockTicker") %></td>
            <td><%= object.getInt("shares") %></td>
            <td><%= object.getInt("stockPrice") %></td>
            <td><%= object.getFloat("profit") %></td>
            <td><%= object.getFloat("buyAmount") %></td>
        </tr>
        <% } %>
    </table>
</body>
</html>
