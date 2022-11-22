<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Stock ${ticker.toUpperCase()}</title>
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
    <h1> ${ticker.toUpperCase()} </h1>
    <h3> Infos: </h3>


    <p>stock price $ ${stockTicker.getFloat("c")}</p>
    <p>today change $ ${stockTicker.get("d")}</p>
    <p>today change % ${stockTicker.get("dp")}</p>
    <p>previous day close% ${stockTicker.get("pc")}</p>

    <h3> transactions: </h3>
    <%
      JSONArray userHistory = (JSONArray) request.getAttribute("userHistory");
    %>
    <table border="1">
      <tr>
         <td>entry date</td>
          <td>order type</td>
         <td>ticker</td>
         <td>shares</td>
         <td>worth</td>
         <td>buy price</td>
      </tr>

        <% if (userHistory.length() != 0) { %>
          <%for (int i = 0; i < userHistory.length(); i++){ %>
          <tr>
            <% JSONObject object = userHistory.getJSONObject(i); %>
              <td><%= object.getString("entryDate") %></td>
              <td><%= object.getString("orderType") %></td>
              <td> <%= object.getString("stockTicker") %></td>
              <td><%= object.getInt("shares") %></td>
              <td><%= object.getFloat("worth") %></td>
              <td><%= object.getFloat("buyPrice") %></td>
          </tr>
            <% } %>
        <% } else {%>
        <p>No Transactions yet</p>
        <% } %>
    </table>

</body>
</html>
