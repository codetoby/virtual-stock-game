package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DataBaseConnection {

    public static HikariDataSource initDatabaseConnectionPool() throws SQLException {

        HikariDataSource dataSource;

        String url = "";
        String username_database = "";
        String password_database = "";

        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username_database);
        dataSource.setPassword(password_database);
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");

        return dataSource;
    }

}
