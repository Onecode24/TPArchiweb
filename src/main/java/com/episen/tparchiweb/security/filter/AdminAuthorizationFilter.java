package com.episen.tparchiweb.security.filter;

import com.episen.tparchiweb.security.annotation.AdminOnly;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@AdminOnly
@Priority(Priorities.AUTHORIZATION)
public class AdminAuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Boolean isAdmin = (Boolean) requestContext.getProperty("isAdmin");

        if (isAdmin == null || !isAdmin) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity("{\"error\": \"Admin access required\"}")
                            .build()
            );
        }
    }
}
