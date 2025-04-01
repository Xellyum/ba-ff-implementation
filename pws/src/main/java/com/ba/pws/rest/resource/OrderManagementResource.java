package com.ba.pws.rest.resource;

import com.ba.pws.featureflag.FeatureFlagConfigProvider;
import com.ba.pws.featureflag.model.FeatureFlag;
import com.ba.pws.persistence.model.Item;
import com.ba.pws.persistence.model.ResourceType;
import com.ba.pws.service.QualityCheckService;
import com.ba.pws.service.ResourceManagementService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Logger;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderManagementResource {

    @Inject
    ResourceManagementService resourceManagementService;

    @Inject
    QualityCheckService qualityCheckService;

    private static final Logger LOGGER = Logger.getLogger(OrderManagementResource.class.getName());

    @POST
    @Transactional
    public Response createItems(List<Item> items) {
        Item.persist(items);
        LOGGER.info("Created new items: " + items);
        return Response.status(Response.Status.CREATED).entity(items).build();
    }

    @GET
    @Path("/all")
    public Response getAllItems() {
        var items = Item.findAll().list();
        return Response.ok(items).build();
    }

    @GET
    @Path("/{id}")
    public Response getItem(@PathParam("id") Long itemId) {
        var item = Item.findById(itemId);
        return Response.ok(item).build();
    }

    @GET
    @Path("/resources/{type}")
    public Response getResourceInventory(@PathParam("type") ResourceType type) {
        if (!FeatureFlagConfigProvider.isFeatureEnabled(FeatureFlag.RESOURCE_MANAGEMENT_ENABLED)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(resourceManagementService.getCurrentStock(type)).build();
    }

    @GET
    @Path("/quality-report")
    public Response getQualityCheckReport() {
        if (!FeatureFlagConfigProvider.isFeatureEnabled(FeatureFlag.QUALITY_CHECK_ENABLED)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var damagedItems = qualityCheckService.getQualityCheckReport();
        return Response.ok(damagedItems).build();
    }
}
