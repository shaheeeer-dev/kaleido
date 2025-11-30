package com.kaleido.tests;

import com.kaleido.db.DatabaseConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Database connection successful!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}