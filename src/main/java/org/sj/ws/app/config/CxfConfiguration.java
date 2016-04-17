package org.sj.ws.app.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.model.wadl.WadlGenerator;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
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
    public Bus bus() {
        return SpringBusFactory.getDefaultBus(true);
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

    /**
     * WADL generator. Just for demo.
     * @return
     */
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

        // Find all beans annotated with @Providers
        List<Object> providers = new ArrayList<Object>(ctx.getBeansWithAnnotation(Provider.class).values());
        providers.add(wadlGenerator());

        /** Register extension mapping */
        Map<Object, Object> extensionMappings = new HashMap<>();
        extensionMappings.put("xml", "application/xml");
        extensionMappings.put("json", "application/json");

        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setBus(bus());
        factory.setAddress("/rest");
        factory.setServiceBeans(serviceBeans);
        factory.setProviders(providers);
        factory.setExtensionMappings(extensionMappings);
        Server server = factory.create();
        return server;
    }
}
