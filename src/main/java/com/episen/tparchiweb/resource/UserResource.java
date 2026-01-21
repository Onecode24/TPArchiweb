package com.episen.tparchiweb.resource;

import com.episen.tparchiweb.dto.UserDTO;
import com.episen.tparchiweb.model.User;
import com.episen.tparchiweb.repository.UserRepository;
import com.episen.tparchiweb.security.annotation.AdminOnly;
import com.episen.tparchiweb.security.annotation.Secured;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @GET
    @Path("/{id}")
    @Secured
    public Response getUser(@PathParam("id") Long id,
                            @Context ContainerRequestContext requestContext) {
        Long currentUserId = (Long) requestContext.getProperty("userId");
        Boolean isAdmin = (Boolean) requestContext.getProperty("isAdmin");

        if (!id.equals(currentUserId) && !isAdmin) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"Access denied\"}")
                    .build();
        }

        User user = userRepository.findById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(new UserDTO(user)).build();
    }

    @GET
    @Secured
    @AdminOnly
    public Response getAllUsers() {
        return Response.ok("{\"message\": \"List all users - Admin only\"}").build();
    }
}
