package org.K03G04Tubes2;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface NasabahService {

    @WebMethod
    boolean isValidNasabahNumber(@WebParam(name = "account_num") int accNum); //layanan1

    @WebMethod
    int createNasabahVirtualAccount(@WebParam(name = "account_num") int accNum); //layanan4

    //layanan 3
    @WebMethod
    void createTransaksiTransfer(@WebParam(name = "account_num_pengirim") int acc_num_pengirim, @WebParam(name = "account_numorva_penerima") int acc_numorva_pwnwrima, @WebParam(name = "jlh_uang") int jlh_uang);

}
