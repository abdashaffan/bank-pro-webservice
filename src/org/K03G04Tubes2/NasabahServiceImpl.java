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
    public int createTransaksiTransfer( int acc_num_pengirim, int acc_numorva_penerima, int jlh_uang){ //layanan3
        if(db.isValidAccountNum(acc_num_pengirim) && (db.isValidAccountNum(acc_numorva_penerima) || (db.getAccountNumberByVA(acc_numorva_penerima) == acc_numorva_penerima) ) ){
            if (db.createTransaksiAccountNum(acc_num_pengirim, acc_numorva_penerima, jlh_uang) == 1){
                return 1; //Transaksi Succes
            } else {
                return -1; //Transaksi gagal atau saldo tidak cukup
            }
        } else {
            return -999; //Account number pengirim atau penerima salah
        }
    }
}
