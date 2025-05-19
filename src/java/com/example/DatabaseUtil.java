package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseUtil {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        String url = "jdbc:mysql://localhost:3306/stock_control";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }
}
