package org.K03G04Tubes2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.sql.SQLException;

@WebService
public interface NasabahService {

    @WebMethod
    boolean isValidNasabahNumber(@WebParam(name = "account_number") int accNum) throws SQLException; //layanan1

    @WebMethod
    int createNasabahVirtualAccount(@WebParam(name = "account_number") int accNum) throws SQLException; //layanan4


}
