package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String URL = "jdbc:mysql://localhost:3306/pidevfinal";
    final String USER = "root";
    final String PASSWORD = "";
    private Connection connection;
    private static MyDatabase instance;
    private MyDatabase() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevfinal", "root", "");
            System.out.println("Connected");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }

        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}


