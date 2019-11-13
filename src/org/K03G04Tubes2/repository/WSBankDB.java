package org.K03G04Tubes2.repository;

import java.sql.*;

public class WSBankDB {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String USER = "abda";
    private static final String PASS = "abda";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ws_bank?serverTimezone=UTC";
    private static Statement stmt;
    private static ResultSet rs;

    public WSBankDB() {
    }

    public WSBankDB(String mySQLStringURI) throws SQLException, ClassNotFoundException {
        try {
            Connection connection = DriverManager.getConnection(mySQLStringURI, USER, PASS);
            stmt = connection.createStatement() ;
            System.out.println("Connected to Database");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
