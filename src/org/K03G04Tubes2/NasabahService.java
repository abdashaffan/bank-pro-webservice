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

}
