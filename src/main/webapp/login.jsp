<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
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
