<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<ul>
    <li><a href="/portfolio">portfolio</a></li>
    <li><a href="/order">order</a></li>
    <li><a href="/register">register</a></li>
    <li><a href="/login">login</a></li>
</ul>
<h1>Login Form</h1>
<form action="${pageContext.request.contextPath}/login" method="POST">
    <label for="email">email</label><br>
    <input type="text" id="email" name="email"><br>
    <label for="password">password</label><br>
    <input type="text" id="password" name="password"><br>
    <input type="submit" value="Submit">
</form>
<h3>
    ${error}
</h3>
<h3>
    ${success}
</h3>
</body>
</html>
