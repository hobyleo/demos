package com.hoby.config;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import com.hoby.service.HiupBaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author hoby
 * @since 2023-03-06
 */
@Configuration
public class WebServiceConfig {

    @Bean(name = SpringBus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public Endpoint endpoint(HiupBaseService hiupBaseService) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), hiupBaseService);
        endpoint.publish("/HiupBaseService");
        return endpoint;
    }

}
