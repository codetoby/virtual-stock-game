package com.example.big.utils;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DataBaseConnection {

    public static HikariDataSource initDatabaseConnectionPool() throws SQLException {

        HikariDataSource dataSource;

        String url = "jdbc:mariadb://188.68.41.89:3308/java";
        String username_database = "toby";
        String password_database = "AktienBotTobyCarl";

        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username_database);
        dataSource.setPassword(password_database);
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");

        return dataSource;
    }

}
