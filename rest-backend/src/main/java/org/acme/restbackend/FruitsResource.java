package org.acme.restbackend;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import org.jboss.logging.Logger;

@Path("/fruits")
public class FruitsResource {

    @RestClient
    FruitsService fruitsService;

    private static final Logger LOGGER = Logger.getLogger(FruitsResource.class.getName());

    @GET
    public Set<Fruit> all() {
        LOGGER.info("all() called.");
        return fruitsService.getAll();
    }
    
    @GET
    @Path("/all-async")
    public CompletionStage<Set<Fruit>> allAsync() {
        LOGGER.info("all-async() called.");
        return fruitsService.getAllAsync();
    }
}
