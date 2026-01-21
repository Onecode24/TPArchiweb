package com.episen.tparchiweb.controller;

import java.util.List;

import com.episen.tparchiweb.dto.CreateOrderRequest;
import com.episen.tparchiweb.dto.OrderResponse;
import com.episen.tparchiweb.service.OrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {
    
    @Inject
    private OrderService orderService;
    
    @POST
    public Response createOrder(CreateOrderRequest request) {
        try {
            
            OrderResponse order = orderService.createOrder(request);
            
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
    public Response getOrder(@PathParam("id") Long id) {
        try {
            OrderResponse order = orderService.findById(id);
            
            return Response.ok(order).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    @GET
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
