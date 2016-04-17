package org.sj.ws.app.web.rest;

import org.sj.ws.app.model.Tests;
import org.sj.ws.app.service.TestService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Rest Resource for Test
 */
@Path("/tests")
public class RestTestResource {

    private TestService service;

    public RestTestResource(TestService service){
        this.service = service;
    }

    /**
     * XML marshalling is not working for generic types. We need to wrap them. GenericEntity is not working.
     * @return
     */
    @GET
    public Response getTests(){
        return Response.ok(new Tests(this.service.getTests())).build();
    }
}
