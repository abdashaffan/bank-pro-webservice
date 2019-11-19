package org.K03G04Tubes2;

import org.K03G04Tubes2.model.Nasabah;
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
    public Nasabah getNasabah(int accNum){ //layanan 2

       return db.getNasabah(accNum);

    }
    @Override
    public int createNasabahVirtualAccount( int accNum){ //layanan4
        if (!db.isValidAccountNum(accNum)) {
            return -1;
        }
        return db.createVirtualAccountNumber(accNum);
    }

    @Override
    public boolean isTransactionExist(int toAcc,int nominal, String begin_date, String end_date){
        return true;
    }

}
