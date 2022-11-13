package com.example.big;

import com.example.big.utils.CreateNewPortfolio;
import com.example.big.utils.DataBaseConnection;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


@WebServlet(name = "Register", urlPatterns = "/register")
public class Register extends HttpServlet {

    private static HikariDataSource dataSource;
    private static void closeDatabaseConnectionPool() {
        dataSource.close();
    }
    protected String createID() {

        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < 30; i++) {
            String e = chars.split("")[(int) Math.floor(Math.random() * chars.length())];
            output.append(e);
        }
        return output.toString();
    }

    public void init() {
        try {
            dataSource = DataBaseConnection.initDatabaseConnectionPool();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || email == null || password == null || confirmPassword == null) {
            request.setAttribute("error", "Input Values are incorrect");
            doGet(request, response);
        } else if (!email.contains("@")) {
            request.setAttribute("error", "Email is not correct");
            doGet(request, response);
        } else if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords are not matching");
            doGet(request, response);
        } else {

           String ID = createID();

            try (Connection connection = dataSource.getConnection()) {
                boolean find = false;
                try (PreparedStatement statement = connection.prepareStatement("""
                SELECT email from users WHERE email = ?
            """)) {
                    statement.setString(1, email);
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        String find_email = resultSet.getString(1);
                        if (Objects.equals(find_email, email)) find = true;
                        }
                }
            if (!find) {
                try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO users(id, username, email, password)
                        VALUES (?, ?, ?, ?)
                    """)) {
                        statement.setString(1, ID);
                        statement.setString(2, username);
                        statement.setString(3, email);
                        statement.setString(4, password);
                        statement.execute();
                    }

                    new CreateNewPortfolio(email);

                } else {
                    request.setAttribute("error", "the specified email is already registered");
                    doGet(request, response);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            response.sendRedirect("/login");
        }
    }

    @Override
    public void destroy() {
        closeDatabaseConnectionPool();
        super.destroy();
    }
}