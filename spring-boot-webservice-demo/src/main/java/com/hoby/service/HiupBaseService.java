package com.hoby.service;


import com.hoby.model.Request;
import com.hoby.model.Response;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author hoby
 * @since 2023-03-06
 */
@WebService(name = "HiupBaseService", targetNamespace = "http://www.cowinhealth.com/hiupReceiveData")
public interface HiupBaseService {

    @WebResult(name = "response", targetNamespace = "http://www.cowinhealth.com/hiupReceiveData")
    @WebMethod(operationName = "hiupReceiveData", action = "http://www.cowinhealth.com/hiupReceiveData")
    Response hiupReceiveData(
            @WebParam(name = "request", targetNamespace = "http://www.cowinhealth.com/hiupReceiveData")
            Request data);

}
