package org.sj.ws.app.web.soap;

import org.sj.ws.app.model.Test;
import org.sj.ws.app.service.TestService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by Sangram on 16-04-2016.
 */
@WebService
public class SoapTestResource {

    private TestService service;

    public SoapTestResource(TestService service){
        this.service = service;
    }

    @WebMethod
    public List<Test> getTests() {
        return this.service.getTests();
    }
}
