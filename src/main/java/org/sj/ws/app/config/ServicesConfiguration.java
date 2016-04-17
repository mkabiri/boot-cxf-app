package org.sj.ws.app.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.sj.ws.app.service.TestService;
import org.sj.ws.app.web.rest.RestTestResource;
import org.sj.ws.app.web.soap.SoapTestResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

/**
 * Declaration of Web Services and their dependencies
 */
@Configuration
public class ServicesConfiguration {

    @Bean
    public TestService testService(){
        return new TestService();
    }

    @Bean
    public SoapTestResource soapTestResource(TestService testService){
        return new SoapTestResource(testService);
    }

    @Bean
    public RestTestResource restTestResource(TestService testService){
        return new RestTestResource(testService);
    }


    /**
     * This is just an example. Most of the time services are created from WSDL so we don't have to create QName, address etc.
     * Although one can override these here.
     */
    @Bean
    public Endpoint testEndPoint(Bus bus, SoapTestResource soapTestResource){
        EndpointImpl endpoint = new EndpointImpl(bus, soapTestResource);
        endpoint.setAddress("/soap/tests");
        endpoint.setServiceName(new QName("urn", "TestResource", ""));
        endpoint.publish();
        return endpoint;
    }
}
