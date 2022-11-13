package com.example.big;

import com.example.big.utils.DataBaseConnection;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "Login", urlPatterns = "/login")
public class Login extends HttpServlet {



    private static HikariDataSource dataSource;

    private static void closeDatabaseConnectionPool() {
        dataSource.close();
    }

    public void init() {
        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean validUser(String email, String password) {

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("""
                SELECT email, password from users WHERE email = ?
            """)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String find_email = resultSet.getString(1);
                    String find_password = resultSet.getString(2);
                    if (email.equals(find_email)) {
                        return password.equals(find_password);
                    } else return false;
                }
            return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (validUser(email, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("id", email);
            response.sendRedirect("/");
        } else {
            request.setAttribute("error", "Email or Password is incorrect");
            doGet(request, response);
        }
    }

    @Override
    public void destroy() {
        closeDatabaseConnectionPool();
        super.destroy();
    }
}