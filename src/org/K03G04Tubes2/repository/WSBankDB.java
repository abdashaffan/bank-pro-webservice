package org.K03G04Tubes2.repository;

import java.sql.*;

public class WSBankDB {
//    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String USER = "abda";
    private static final String PASS = "abda";
    private static Connection connection;
    private static PreparedStatement stmt;
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/ws_bank?serverTimezone=UTC";

    public WSBankDB() {
    }

    public WSBankDB(String mySQLStringURI) {
        try {
            connection = DriverManager.getConnection(mySQLStringURI, USER, PASS);
            System.out.println("Connected to Database URL:" + mySQLStringURI);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getNasabahAccountById(int id) throws  SQLException {
        String query = "SELECT account_num FROM nasabah WHERE id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();
        if (result.next()){
            return result.getInt("account_num");
        }
        return -1;
    }

    private boolean isValidNasabah(int accNum) throws SQLException {
        String query = "SELECT * FROM nasabah WHERE account_num = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, accNum);
        ResultSet result = stmt.executeQuery();
        return result.next();
    }

    private int createVirtualAccountNumber(int accNum) throws SQLException {
        //prekondisi: account_num udah valid dan udah ada di db sebelumnya
        int maxAccountNum = 999999;
        int minAccountNum = 100001;
        int virtualAccNum  = (int)((Math.random()*((maxAccountNum-minAccountNum)+1))+minAccountNum);
        String query = "INSERT INTO virtual_account (virtual_account_num, account_num) VALUES (?, ?)";
        stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, virtualAccNum);
        stmt.setInt(2, accNum);
        int executeStatus = stmt.executeUpdate();
        if (executeStatus == 0 || executeStatus == -1) {
            throw new SQLException("Failed to create new virtual account for account number" + accNum);
        }
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

//    Buat tester aja
//    public static void main(String[] args) throws SQLException {
//        WSBankDB db = new WSBankDB("jdbc:mysql://localhost:3306/ws_bank?serverTimezone=UTC");
//
//        System.out.println( db.getNasabahAccountById(1));
//        System.out.println( db.getNasabahAccountById(2));
//        System.out.println( db.getNasabahAccountById(3));
//        System.out.println( db.isValidNasabah(10001));
//        System.out.println( db.isValidNasabah(99999));
//        System.out.println( db.createVirtualAccountNumber(10001));
//        System.out.println( db.createVirtualAccountNumber(10002));
//    }

}
