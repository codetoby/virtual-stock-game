<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Buy</title>
</head>
<body>
<h3>
  Buy / Sell Route
</h3>

<form action="/order" method="post">
    <label for="OrderType">Choose Buy OR Sell:</label>
    <input list="OrderTypes" name="orderType" id="OrderType">
    <datalist id="OrderTypes">
        <option value="buy">
        <option value="sell">
    </datalist><br>
    <label for="ticker">Stock Ticker</label><br>
    <input type="text" id="ticker" name="ticker"><br>
    <label for="shares">Shares</label><br>
    <input type="text" id="shares" name="shares"><br>
    <input type="submit">
</form>
<h1>${message}</h1>
</body>
</html>
