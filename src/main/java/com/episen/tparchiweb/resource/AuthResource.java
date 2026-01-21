package com.episen.tparchiweb.resource;

import com.episen.tparchiweb.dto.LoginRequest;
import com.episen.tparchiweb.dto.LoginResponse;
import com.episen.tparchiweb.dto.RegisterRequest;
import com.episen.tparchiweb.dto.UserDTO;
import com.episen.tparchiweb.exception.AuthenticationException;
import com.episen.tparchiweb.exception.RegistrationException;
import com.episen.tparchiweb.security.annotation.Secured;
import com.episen.tparchiweb.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            LoginResponse response = authService.login(
                    request.getUsername(),
                    request.getPassword()
            );
            return Response.ok(response).build();
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            if (request == null) throw new RegistrationException("Request is empty");
            UserDTO user = authService.register(request);
            return Response.status(Response.Status.CREATED).entity(user).build();
        } catch (RegistrationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/me")
    @Secured
    public Response getCurrentUser(@Context ContainerRequestContext requestContext) {
        Long userId = (Long) requestContext.getProperty("userId");
        String username = (String) requestContext.getProperty("username");
        Boolean isAdmin = (Boolean) requestContext.getProperty("isAdmin");

        return Response.ok()
                .entity("{\"id\": " + userId +
                        ", \"username\": \"" + username +
                        "\", \"isAdmin\": " + isAdmin + "}")
                .build();
    }
}
