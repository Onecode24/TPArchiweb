package com.episen.tparchiweb.resource;

import java.util.List;
import java.util.Objects;

import com.episen.tparchiweb.dto.CreateOrderRequest;
import com.episen.tparchiweb.dto.OrderResponse;
import com.episen.tparchiweb.security.annotation.AdminOnly;
import com.episen.tparchiweb.security.annotation.Secured;
import com.episen.tparchiweb.service.OrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    
    @Inject
    private OrderService orderService;
    
    @POST
    @Secured
    public Response createOrder(CreateOrderRequest request, @Context ContainerRequestContext requestContext) {
        Long currentUserId = (Long) requestContext.getProperty("userId");
        try {
            OrderResponse order = orderService.createOrder(request, currentUserId);
            
            return Response.status(Response.Status.CREATED)
                    .entity(order)
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Path("/{id}")
    @Secured
    public Response getOrder(@PathParam("id") Long id, @Context ContainerRequestContext requestContext) {
        Long currentUserId = (Long) requestContext.getProperty("userId");
        Boolean isAdmin = (Boolean) requestContext.getProperty("isAdmin");

        try {
            OrderResponse order = orderService.findById(id);

            if (!isAdmin || !Objects.equals(order.getUserId(), currentUserId)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\": \"Access denied\"}")
                        .build();
            }

            return Response.ok(order).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
    @Secured
    @AdminOnly
    public List<OrderResponse> getAllOrders() {
        try {
            List<OrderResponse> orders = orderService.findAll();
            
            return orders;
            
        } catch (Exception e) {
            throw new WebApplicationException("Error retrieving orders: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DELETE
    @Path("/{id}")
    @Secured
    @AdminOnly
    public Response deleteOrder(@PathParam("id") Long id) {
        try {
            orderService.deleteOrder(id);
            
            return Response.status(Response.Status.NO_CONTENT).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
