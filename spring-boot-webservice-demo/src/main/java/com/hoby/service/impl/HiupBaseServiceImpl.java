package com.hoby.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.hoby.model.Request;
import com.hoby.model.Response;
import com.hoby.model.ResponseHeader;
import com.hoby.service.HiupBaseService;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

/**
 * @author hoby
 * @since 2023-03-06
 */
@Slf4j
@Service
@WebService(
        serviceName = "HiupBaseService",
        targetNamespace = "http://www.cowinhealth.com/hiupReceiveData",
        endpointInterface = "org.example.webservice.service.HiupBaseService")
public class HiupBaseServiceImpl implements HiupBaseService {

    @Override
    public Response hiupReceiveData(Request data) {
        log.info("Executing operation hiupReceiveData");
        String str = data.getRequestBody().trim();
        System.out.println(str);
        System.out.println(str.length());
        try {
            Response response = new Response();
            ResponseHeader responseHeader = new ResponseHeader();
            responseHeader.setMsgId("001");
            responseHeader.setMsgType("receive");
            response.setResponseHeader(responseHeader);
            response.setResponseBody("ok");
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
