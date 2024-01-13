package org.acme.restbackend;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.Set;
import java.util.concurrent.CompletionStage;

@Path("/fruits")
public class FruitsResource {

    @RestClient
    FruitsService fruitsService;

    @GET
    public Set<Fruit> all() {
        return fruitsService.getAll();
    }
    
    @GET
    @Path("/all-async")
    public CompletionStage<Set<Fruit>> allAsync() {
        return fruitsService.getAllAsync();
    }
}
