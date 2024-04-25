package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private Connection connection;
    private static MyDatabase instance;

    private MyDatabase() {
        try {
            // Adjust port and database name
            String URL = "jdbc:mysql://localhost:3306/pidevfinal";
            // Replace with your MySQL username
            String USER = "root";
            // Replace with your MySQL password
            String PASSWORD = "";
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.err.println("Connection to database failed: " + e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if(instance == null)
            instance = new MyDatabase();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
