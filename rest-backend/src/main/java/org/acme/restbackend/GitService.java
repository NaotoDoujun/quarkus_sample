package org.acme.restbackend;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.client.api.ClientMultipartForm;

@Path("/git")
@RegisterRestClient(configKey = "restgit-api")
public interface GitService {
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    Map<String, String> add(ClientMultipartForm dataParts);

    @POST
    @Path("/commit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Set<String> commit(@RestForm String message);

}