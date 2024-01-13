package org.acme.restbackend;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/fruits")
@RegisterRestClient(configKey = "restdb-api")
public interface FruitsService {

    @GET
    Set<Fruit> getAll();

    @GET
    CompletionStage<Set<Fruit>> getAllAsync();

}
