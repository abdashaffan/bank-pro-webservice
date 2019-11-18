package org.K03G04Tubes2.repository;

import java.sql.*;

public class WSBankDB {
//    private static final String PUBLIC_EC2_DNS = "ec2-52-23-241-205.compute-1.amazonaws.com";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/ws_bank?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
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

    public boolean isValidAccountNum(int accNum){
        try {
            String query = "SELECT * FROM nasabah WHERE account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, accNum);
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isValidVirtualAccountNum(int accNum){
        try {
            String query = "SELECT * FROM virtual_account WHERE virtual_account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, accNum);
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createVirtualAccountNumber(int accNum){
        //prekondisi: accNum UDAH VALID (udah ada di db sebelumnya)
        try {
            int virtualAccNum = generateVirtualAccNum();
            String query = "INSERT INTO virtual_account (virtual_account_num, account_num) VALUES (?, ?)";
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, virtualAccNum);
            stmt.setInt(2, accNum);
            int executeStatus = stmt.executeUpdate();
            if (executeStatus == 0 || executeStatus == -1) {
                throw new SQLException("Failed to create new virtual account for account number" + accNum);
            }
            ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {

                    int id = generatedKeys.getInt(1);
                    return getVirtualAccountById(id);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
        } catch (SQLException e) {
            e.printStackTrace();
            return -999;
        }
    }

    private int getVirtualAccountById(int id)  {
        try {
            String query = "SELECT virtual_account_num FROM virtual_account WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()){
                return result.getInt("virtual_account_num");
            }
            return -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -999;
        }
    }

    public int getAccountNumberByVA(int acc_number)  {
        try {
            String query = "SELECT account_num FROM virtual_account WHERE virtual_account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, acc_number);
            ResultSet result = stmt.executeQuery();
            if (result.next()){
                return result.getInt("account_num");
            }
            return -1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -999;
        }
    }

    private int generateVirtualAccNum() {
        int maxVAccountNum = 999999;
        int minVAccountNum = 100001;
        return (int)((Math.random()*((maxVAccountNum-minVAccountNum)+1))+minVAccountNum);
    }

    public int createTransaksiAccountNum(int acc_num_pengirim, int acc_numorva_penerima, int jlh_uang){
        try {
            //Query untuk data balance pengirim
            String query = "SELECT balance FROM nasabah WHERE account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, acc_num_pengirim);
            ResultSet result = stmt.executeQuery();

            if (result.next()){
                //Simpan data Balance Pengirim
                int balance_pengirim = result.getInt("balance");

                //Cek saldo pengirim cukup atau tidak
                if (balance_pengirim >= jlh_uang) {
                    //Query untuk data balance penerima
                    query = "SELECT balance FROM nasabah WHERE account_num = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setInt(1, acc_num_pengirim);
                    result = stmt.executeQuery();

                    if (result.next()){
                        //Simpan data Balance Pengirim
                        int balance_penerima = result.getInt("balance");

                        String query1 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        String query2 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        int new_balance_pengirim = balance_pengirim - jlh_uang;
                        int new_balance_penerima = balance_penerima + jlh_uang;

                        String query_transaksi = "INSERT INTO transaksi(sender, receiver, amount) values(?, ?, ?)";

                        stmt = connection.prepareStatement(query1);
                        stmt.setInt(1,new_balance_pengirim);
                        stmt.setInt(2,acc_num_pengirim);
                        result = stmt.executeQuery();

                        stmt = connection.prepareStatement(query2);
                        stmt.setInt(1,new_balance_penerima);
                        stmt.setInt(2,acc_numorva_penerima);
                        result = stmt.executeQuery();

                        stmt = connection.prepareStatement(query_transaksi);
                        stmt.setInt(1,acc_num_pengirim);
                        stmt.setInt(2,acc_numorva_penerima);
                        stmt.setInt(3,jlh_uang);
                        result = stmt.executeQuery();

                        return 1; // query berhasil
                    }
                }
            }
            return -999; //query error
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
