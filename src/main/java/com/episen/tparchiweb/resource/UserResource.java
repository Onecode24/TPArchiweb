package com.episen.tparchiweb.resource;

import com.episen.tparchiweb.dto.UpdateUserRequest;
import com.episen.tparchiweb.dto.UserDTO;
import com.episen.tparchiweb.model.Product;
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

import java.util.ArrayList;
import java.util.List;

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

    @PUT
    @Path("/{id}")
    @Secured
    public Response update(@PathParam("id") Long id, UpdateUserRequest updatedUser, @Context ContainerRequestContext requestContext) {
        Long currentUserId = (Long) requestContext.getProperty("userId");
        Boolean isAdmin = (Boolean) requestContext.getProperty("isAdmin");

        if (!id.equals(currentUserId) && !isAdmin) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"Access denied\"}")
                    .build();
        }

        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null) existingUser.setPassword(updatedUser.getPassword());
        if (isAdmin && updatedUser.getIsAdmin() != null) {
            existingUser.setIsAdmin(updatedUser.getIsAdmin());
        }

        existingUser = userRepository.update(existingUser);
        return Response.ok(new UserDTO(existingUser)).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    @AdminOnly
    public Response delete(@PathParam("id") Long id) {
        User user = userRepository.findById(id);

        if (user != null) {
            userRepository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Secured
    @AdminOnly
    public Response getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        List<UserDTO> usersDTO = new ArrayList<>();

        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }

        return Response.ok(usersDTO).build();
    }
}
