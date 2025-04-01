package com.ba.ffms.rest.resource;

import com.ba.ffms.persistence.model.Environment;
import com.ba.ffms.rest.model.FeatureFlagDTO;
import com.ba.ffms.service.FeatureFlagService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/flag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeatureFlagResource {

    @Inject
    FeatureFlagService featureFlagService;

    @GET
    public Response getFeatureFlags(@QueryParam("feature") Optional<String> feature, @QueryParam("env") Optional<String> environment) {
        try {
            if (feature.isPresent() && environment.isPresent()) {
                var env = Environment.valueOf(environment.get().toUpperCase());
                return Response.ok(featureFlagService.getFeatureFlag(feature.get(), env)).build();
            }
            if (feature.isPresent()) {
                return Response.ok(featureFlagService.getFeatureFlagsByFeature(feature.get())).build();
            }
            if (environment.isPresent()) {
                var env = Environment.valueOf(environment.get().toUpperCase());
                return Response.ok(featureFlagService.getFeatureFlagsByEnvironment(env)).build();
            }
            return Response.ok(featureFlagService.getAllFeatureFlags()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid environment value: " + environment.orElse("")).build();
        }
    }

    @POST
    public Response createFeatureFlag(@NotNull FeatureFlagDTO featureFlagDTO) {
        var featureFlag = featureFlagService.createFeatureFlag(featureFlagDTO);

        if (featureFlag == null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Feature Flag already exists: " + featureFlagDTO.feature())
                    .build();
        }
        return Response.status(Response.Status.CREATED).entity(featureFlag).build();
    }

    @PUT
    public Response editFeatureFlag(@NotNull FeatureFlagDTO featureFlagDTO) {
        var featureFlag = featureFlagService.editFeatureFlag(featureFlagDTO);
        if (featureFlag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.accepted().build();
    }

    @DELETE
    @Path("/{feature}")
    public Response deleteFeatureFlag(@PathParam("feature") String feature) {
        var featureFlag = featureFlagService.deleteFeatureFlag(feature);

        if (featureFlag == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/event")
    public Response createFeatureFlagEvents() {
        featureFlagService.sendFeatureFlagEvents();
        return Response.ok().build();
    }
}
