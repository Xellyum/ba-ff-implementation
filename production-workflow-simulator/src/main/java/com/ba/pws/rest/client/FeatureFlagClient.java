package com.ba.pws.rest.client;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Map;

@Path("/flag")
@RegisterRestClient(configKey = "feature-flag-service-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface FeatureFlagClient {

    @GET
    Map<String, Boolean> getFeatureFlags(@QueryParam("env") String environment);
}
