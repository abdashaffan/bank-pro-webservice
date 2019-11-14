package org.K03G04Tubes2.repository;

import java.sql.*;

public class WSBankDB {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/ws_bank?serverTimezone=UTC";
    private static final String DB_USER = "abda";
    private static final String DB_PASS = "abda";
    private static Connection connection;
    private static PreparedStatement stmt;


    public WSBankDB() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Connected to Database URL:" + DB_URL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValidAccountNum(int accNum) throws SQLException {
        String query = "SELECT * FROM nasabah WHERE account_num = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, accNum);
        ResultSet result = stmt.executeQuery();
        return result.next();
    }

    public int createVirtualAccountNumber(int accNum) throws SQLException {
        //prekondisi: accNum UDAH VALID (udah ada di db sebelumnya)
        int virtualAccNum = generateVirtualAccNum();
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

                int id = generatedKeys.getInt(1);
                return getVirtualAccountById(id);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    private int getVirtualAccountById(int id) throws  SQLException {
        String query = "SELECT virtual_account_num FROM virtual_account WHERE id = ?";
        stmt = connection.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet result = stmt.executeQuery();
        if (result.next()){
            return result.getInt("virtual_account_num");
        }
        return -1;
    }

    private int generateVirtualAccNum() {
        int maxVAccountNum = 999999;
        int minVAccountNum = 100001;
        return (int)((Math.random()*((maxVAccountNum-minVAccountNum)+1))+minVAccountNum);
    }
}
