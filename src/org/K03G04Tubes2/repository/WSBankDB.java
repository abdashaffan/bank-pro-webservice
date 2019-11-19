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

    public int cekSaldoMencukupi(int acc_num, int jlh_uang){
        try {
            //Query untuk data balance
            String query = "SELECT balance FROM nasabah WHERE account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, acc_num);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int balance = result.getInt("balance");
                if (balance - jlh_uang >= 0) {
                    return 1; //Saldo cukup
                } else {
                    return 0; //Saldo tidak cukup
                }
            } else {
                return -1; // hasil query tidak ada
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -999;
        }
    }

    public int createTransaksiAccount(int acc_num_pengirim, int acc_numorva_penerima, int jlh_uang){
        try {
            //Query untuk data balance pengirim
            String query = "SELECT balance FROM nasabah WHERE account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, acc_num_pengirim);
            ResultSet result = stmt.executeQuery();

            if (result.next()){
                //Simpan data Balance Pengirim
                int balance_pengirim = result.getInt("balance");
                System.out.println("balance_pengirim = " + balance_pengirim);

                //Cek saldo pengirim cukup atau tidak
                if (cekSaldoMencukupi(acc_num_pengirim, jlh_uang) == 1) {
                    //Query untuk data balance penerima
                    String query0 = "SELECT balance FROM nasabah WHERE account_num = ?";
                    PreparedStatement stmt0 = connection.prepareStatement(query0);
                    stmt0.setInt(1, acc_num_pengirim);
                    ResultSet result0 = stmt0.executeQuery();

                    if (result0.next()) {
                        //Simpan data Balance Pengirim
                        int balance_penerima = result0.getInt("balance");
                        System.out.println("balance_penerima" + balance_penerima);

                        String query1 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        String query2 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        int new_balance_pengirim = balance_pengirim - jlh_uang;
                        int new_balance_penerima = balance_penerima + jlh_uang;

                        String query_transaksi = "INSERT INTO transaksi(sender, receiver, amount) values(?, ?, ?)";

                        PreparedStatement stmt1 = connection.prepareStatement(query1);
                        stmt1.setInt(1, new_balance_pengirim);
                        stmt1.setInt(2, acc_num_pengirim);
                        int result1 = stmt1.executeUpdate();

                        if (result1 == 1) {
                            System.out.println("update balance pengirim" + result1);
                            PreparedStatement stmt2 = connection.prepareStatement(query2);
                            stmt2.setInt(1, new_balance_penerima);
                            stmt2.setInt(2, acc_numorva_penerima);
                            int result2 = stmt2.executeUpdate();

                            if (result2 == 1) {
                                System.out.println("update balance penerima" + result2);
                                PreparedStatement stmt3 = connection.prepareStatement(query_transaksi);
                                stmt3.setInt(1, acc_num_pengirim);
                                stmt3.setInt(2, acc_numorva_penerima);
                                stmt3.setInt(3, jlh_uang);
                                int result3 = stmt3.executeUpdate();
                                if (result3 == 1) {
                                    System.out.println("tambah data transaksi" + result3);
                                    return 1; //Transaksi Succes
                                } else {
                                    return -1; //Hasil query tidak ada
                                }
                            } else {
                                return -1; //Hasil query tidak ada
                            }
                        } else {
                            return -1; //Hasil query tidak ada
                        }
                    } else {
                        return -1; //Hasil query tidak ada
                    }
                } else {
                    return -2; //Saldo tidak cukup
                }
            } else {
                return -1; //Hasil query tidak ada
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -999; //Query Error
        }
    }
}
