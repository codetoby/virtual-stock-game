<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
<ul>
  <li><a href="/portfolio">portfolio</a></li>
  <li><a href="/order">order</a></li>
  <li><a href="/register">register</a></li>
  <li><a href="/login">login</a></li>
</ul>
<h1>
  Register Form
</h1>
<form action="${pageContext.request.contextPath}/register" method="POST">
  <label for="username">username</label><br>
  <input type="text" id="username" name="username"><br>
  <label for="email">email</label><br>
  <input type="text" id="email" name="email"><br>
  <label for="password">password</label><br>
  <input type="text" id="password" name="password"><br>
  <label for="confirmPassword">confirm password</label><br>
  <input type="text" id="confirmPassword" name="confirmPassword"><br>
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
