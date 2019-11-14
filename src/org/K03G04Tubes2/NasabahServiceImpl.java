package org.K03G04Tubes2;

import org.K03G04Tubes2.repository.WSBankDB;

import javax.jws.WebService;
import java.sql.SQLException;

@WebService(serviceName = "BankProServices", endpointInterface = "org.K03G04Tubes2.NasabahService")
public class NasabahServiceImpl implements NasabahService{


    private  WSBankDB db;


    public NasabahServiceImpl() {
        db = new WSBankDB();
    }

    @Override
    public boolean isValidNasabahNumber( int accNum) throws SQLException { //layanan1
        return db.isValidAccountNum(accNum);
    }

    @Override
    public int createNasabahVirtualAccount( int accNum) throws SQLException { //layanan4
        if (!db.isValidAccountNum(accNum)) {
            return -1;
        }
        return db.createVirtualAccountNumber(accNum);
    }
}
