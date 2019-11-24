package org.K03G04Tubes2.repository;

import org.K03G04Tubes2.model.Nasabah;
import org.K03G04Tubes2.model.Transaction;

import java.sql.*;
import java.util.ArrayList;

public class WSBankDB {
//    private static final String PUBLIC_EC2_DNS = "ec2-52-23-241-205.compute-1.amazonaws.com";
    private static final String DB_URL = "jdbc:mysql://database-1.cmqcolum2oyw.us-east-1.rds.amazonaws.com/ws_bank?serverTimezone=UTC&useSSL=false";
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

    public Nasabah getNasabah(int accNum){
        try{
            String query = "SELECT * FROM nasabah WHERE account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1,accNum);
            ResultSet result = stmt.executeQuery();

            // Create and return new nasabah
            if(result.next()==false){
                //empty nasabah
                //use dummy accountNum -999 instead of returning null
                Nasabah n = new Nasabah("",-999);
                return n;
            } else {
                Nasabah n = new Nasabah(result.getString("name"),accNum);
                n.setBalance(result.getInt("balance"));
                // Get va list
                ArrayList<Integer> va_list = getVirtualAccountByUser(accNum);
                // Iterate over list
                for(Integer i:va_list){
                    n.addVirtualAccount(i);
                }

                // Get transaction list
                ArrayList<Transaction> transaction_list = getTransactionsByUser(accNum);
                //Iterate over the list
                for (Transaction t : transaction_list){
                    n.addTransactionHistory(t);
                }
                return n;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
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

    public int getNasabahNumByName(String username) {
        try {
            String query = "SELECT account_num from nasabah WHERE name = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getInt("account_num");
            }
            return -1; //if not found
        }
        catch (SQLException e) {
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

    public int getAccountNumberByVA(int virtual_acc_number)  {
        try {
            String query = "SELECT account_num FROM virtual_account WHERE virtual_account_num = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, virtual_acc_number);
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

    private ArrayList<Integer> getVirtualAccountByUser(int accNum){
        ArrayList<Integer> va_list = new ArrayList<Integer>();
        try {
            String query = "SELECT id FROM virtual_account WHERE account_num=?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1,accNum);
            ResultSet result_set = stmt.executeQuery();
            while(result_set.next()){
                va_list.add(result_set.getInt("id"));
            }
            return va_list;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Transaction> getTransactionsByUser(int accNum){
        ArrayList<Transaction> tra_list = new ArrayList<Transaction>();
        Transaction ta;
        try{
            String query = "SELECT *, (CASE WHEN sender=? THEN 'debit' ELSE 'kredit' END) AS type FROM transaksi WHERE sender=? OR receiver=?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1,accNum);
            stmt.setInt(2,accNum);
            stmt.setInt(3,accNum);
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                System.out.println("HIT !");
                ta = new Transaction(result.getString("type"),
                        result.getInt("amount"),
                        result.getInt("sender"),
                        result.getInt("receiver"),
                        result.getDate("date")
                        );
                tra_list.add(ta);
            };
            return tra_list;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkTransaction(int accNum, int nominal, String begin_date, String end_date){
        try {
            String sql = "SELECT * FROM transaksi WHERE receiver=? AND amount=? AND date>=? AND date<=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1,accNum);
            stmt.setInt(2,nominal);
            stmt.setString(3,begin_date);
            stmt.setString(4,end_date);

            ResultSet result = stmt.executeQuery();
            return result.next();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
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
                    query = "SELECT balance FROM nasabah WHERE account_num = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setInt(1, acc_numorva_penerima);
                    result = stmt.executeQuery();

                    if (result.next()) {
                        //Simpan data Balance penerima
                        int balance_penerima = result.getInt("balance");
                        System.out.println("balance_penerima" + balance_penerima);

                        String query1 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        String query2 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        int new_balance_pengirim = (balance_pengirim - jlh_uang);
                        int new_balance_penerima = (balance_penerima + jlh_uang);

                        String query_transaksi = "INSERT INTO transaksi(sender, receiver, amount) values(?, ?, ?)";

                        stmt = connection.prepareStatement(query1);
                        stmt.setInt(1, new_balance_pengirim);
                        stmt.setInt(2, acc_num_pengirim);
                        int result1 = stmt.executeUpdate();

                        if (result1 == 1) {
                            System.out.println("update balance pengirim" + result1);
                            stmt = connection.prepareStatement(query2);
                            stmt.setInt(1, new_balance_penerima);
                            stmt.setInt(2, acc_numorva_penerima);
                            result1 = stmt.executeUpdate();

                            if (result1 == 1) {
                                System.out.println("update balance penerima" + result1);
                                stmt = connection.prepareStatement(query_transaksi);
                                stmt.setInt(1, acc_num_pengirim);
                                stmt.setInt(2, acc_numorva_penerima);
                                stmt.setInt(3, jlh_uang);
                                result1 = stmt.executeUpdate();
                                if (result1 == 1) {
                                    System.out.println("tambah data transaksi" + result1);
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

    public int createTransaksiVA(int acc_num_pengirim, int va_penerima, int jlh_uang){
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
                    int acc_numorva_penerima = getAccountNumberByVA(va_penerima);
                    //Query untuk data balance penerima
                    query = "SELECT balance FROM nasabah WHERE account_num = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setInt(1, acc_numorva_penerima);
                    result = stmt.executeQuery();

                    if (result.next()) {
                        //Simpan data Balance penerima
                        int balance_penerima = result.getInt("balance");
                        System.out.println("balance_penerima" + balance_penerima);

                        String query1 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        String query2 = "UPDATE nasabah SET balance = ? WHERE account_num = ?";
                        int new_balance_pengirim = (balance_pengirim - jlh_uang);
                        int new_balance_penerima = (balance_penerima + jlh_uang);

                        String query_transaksi = "INSERT INTO transaksi(sender, receiver, amount) values(?, ?, ?)";

                        stmt = connection.prepareStatement(query1);
                        stmt.setInt(1, new_balance_pengirim);
                        stmt.setInt(2, acc_num_pengirim);
                        int result1 = stmt.executeUpdate();

                        if (result1 == 1) {
                            System.out.println("update balance pengirim" + result1);
                            stmt = connection.prepareStatement(query2);
                            stmt.setInt(1, new_balance_penerima);
                            stmt.setInt(2, acc_numorva_penerima);
                            result1 = stmt.executeUpdate();

                            if (result1 == 1) {
                                System.out.println("update balance penerima" + result1);
                                stmt = connection.prepareStatement(query_transaksi);
                                stmt.setInt(1, acc_num_pengirim);
                                stmt.setInt(2, acc_numorva_penerima);
                                stmt.setInt(3, jlh_uang);
                                result1 = stmt.executeUpdate();
                                if (result1 == 1) {
                                    System.out.println("tambah data transaksi" + result1);
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
