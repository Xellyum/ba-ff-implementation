package com.ba.nc.rest.resource;

import com.ba.nc.service.FeatureFlagCacheService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/cache")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeatureFlagCacheResource {

    @Inject
    FeatureFlagCacheService featureFlagCacheService;

    @GET
    public Response getFeatureFlag() {
        var featureFlagConfig = featureFlagCacheService.getLatestFeatureFlagConfig();
        return featureFlagConfig.isEmpty()
                ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(featureFlagConfig).build();
    }

    @POST
    public Response addFeatureFlag(Map<String, Boolean> featureFlagConfig) {
        var featureFlag = featureFlagCacheService.addFeatureFlagConfig(featureFlagConfig);
        return featureFlag == null
                ? Response.status(Response.Status.CONFLICT).build()
                : Response.status(Response.Status.CREATED).entity(featureFlag).build();
    }
}
