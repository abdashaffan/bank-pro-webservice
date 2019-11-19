package org.K03G04Tubes2;

import org.K03G04Tubes2.repository.WSBankDB;

import javax.jws.WebService;

@WebService(serviceName = "bankpro",endpointInterface = "org.K03G04Tubes2.NasabahService")
public class NasabahServiceImpl implements NasabahService{


    private WSBankDB db;


    public NasabahServiceImpl() {
        db = new WSBankDB();
    }

    @Override
    public boolean isValidNasabahNumber( int accNum){ //layanan1
        return db.isValidAccountNum(accNum);
    }

    @Override
    public int createNasabahVirtualAccount( int accNum){ //layanan4
        if (!db.isValidAccountNum(accNum)) {
            return -1;
        }
        return db.createVirtualAccountNumber(accNum);
    }

    @Override
<<<<<<< HEAD
    public int getNasabahRealAccountByName(String username) {
        if (username == "") return -1;
        return db.getNasabahNumByName(username);
    }

=======
    public String createTransaksiTransfer( int acc_num_pengirim, int acc_numorva_penerima, int jlh_uang) { //layanan3
        if(db.isValidAccountNum(acc_num_pengirim) && (db.isValidAccountNum(acc_numorva_penerima) || (db.getAccountNumberByVA(acc_numorva_penerima) == acc_numorva_penerima) ) ) {
            if (db.cekSaldoMencukupi(acc_num_pengirim, jlh_uang) == 1){
                if (db.createTransaksiAccount(acc_num_pengirim, acc_numorva_penerima, jlh_uang) == 1) {
                    return "Transaksi succes";
                } else {
                    return "Proses transaksi error";
                }
            } else {
                 return "Saldo tidak cukup"; //Saldo tidak cukup
            }
        } else {
            return "Account number pengirim atau penerima salah"; //Account number pengirim atau penerima salah
        }
    }
>>>>>>> feature/feature_transaksi
}






