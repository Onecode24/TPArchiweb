package com.episen.tparchiweb.controller;

import com.episen.tparchiweb.model.Order;
import com.episen.tparchiweb.service.OrderService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {
    
    private OrderService service = new OrderService();
    
    @GET
    public List<Order> getAll() {
        return service.getAll();
    }
    
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Order order = service.getById(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(order).build();
    }
    
    @GET
    @Path("/user/{userId}")
    public List<Order> getByUserId(@PathParam("userId") Long userId) {
        return service.getByUserId(userId);
    }
    
    @POST
    public Response create(Order order) {
        Order created = service.create(order);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public Order update(@PathParam("id") Long id, Order order) {
        return service.update(id, order);
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
