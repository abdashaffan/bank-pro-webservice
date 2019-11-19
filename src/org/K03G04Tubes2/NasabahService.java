package org.K03G04Tubes2;

import org.K03G04Tubes2.model.Nasabah;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface NasabahService {

    @WebMethod
    boolean isValidNasabahNumber(@WebParam(name = "account_num") int accNum); //layanan1

    @WebMethod
    Nasabah getNasabah(@WebParam(name = "account_num")int accNum); //layanan2

    @WebMethod
    int createNasabahVirtualAccount(@WebParam(name = "account_num") int accNum); //layanan4

    @WebMethod
    boolean isTransactionExist(
            @WebParam(name="to_account")int toAcc,
            @WebParam(name="nominal")int nominal,
            @WebParam(name="begin_date")String begin_date,
            @WebParam(name="end_date")String end_date
    );// layanan 5

}
