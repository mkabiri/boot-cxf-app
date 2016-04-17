package org.sj.ws.app.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.model.wadl.WadlGenerator;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.sj.ws.app.service.TestService;
import org.sj.ws.app.web.rest.RestTestResource;
import org.sj.ws.app.web.soap.SoapTestResource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CXF Configuration
 */

@Configuration
public class CxfConfiguration {

    @Bean(name="cxfServlet")
    @ConditionalOnMissingBean
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/api/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    @ConditionalOnMissingBean
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public JacksonJsonProvider jsonProvider(ObjectMapper objectMapper) {
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(objectMapper);
        return provider;
    }

    @Bean
    @ConditionalOnMissingBean
    public WadlGenerator wadlGenerator() {
        WadlGenerator wadlGenerator = new WadlGenerator();
        return wadlGenerator;
    }

    @Bean
    @ConditionalOnMissingBean
    public JAXBElementProvider xmlProvider(){
        return new JAXBElementProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public Server jaxRsServer(ApplicationContext ctx) {
        // Find all beans annotated with @Path
        List<Object> serviceBeans = new ArrayList<>(ctx.getBeansWithAnnotation(Path.class).values());
        //serviceBeans.add(restTestResource());

        // Find all beans annotated with @Providers
        List<Object> providers = new ArrayList<Object>(ctx.getBeansWithAnnotation(Provider.class).values());
        providers.add(wadlGenerator());

        Map<Object, Object> extensionMappings = new HashMap<>();
        extensionMappings.put("xml", "application/xml");
        extensionMappings.put("json", "application/json");

        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(springBus());
        factory.setAddress("/rest");
        factory.setServiceBeans(serviceBeans);
        factory.setProviders(providers);
        factory.setExtensionMappings(extensionMappings);
        Server server = factory.create();
        return server;
    }

    @Bean
    public TestService testService(){
        return new TestService();
    }

    @Bean
    public SoapTestResource soapTestResource(){
        return new SoapTestResource(testService());
    }

    @Bean
    public RestTestResource restTestResource(){
        return new RestTestResource(testService());
    }

    @Bean
    public Endpoint testEndPoint(){
        EndpointImpl endpoint = new EndpointImpl(springBus(), soapTestResource());
        endpoint.setAddress("/soap/tests");
        endpoint.setServiceName(new QName("urn", "TestResource", ""));
        endpoint.publish();
        return endpoint;
    }
}
