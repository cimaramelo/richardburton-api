package br.edu.ifrs.canoas.richardburton.session;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Provider
@RequiresAuthentication
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        Response response = Response.status(Response.Status.UNAUTHORIZED).build();

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
            requestContext.abortWith(response);
            return;
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        boolean unauthorized;

        try {

            Claims claims = JWT.decodeToken(token);

            Method method = resourceInfo.getResourceMethod();
            RequiresAuthentication annotation = method.getAnnotation(RequiresAuthentication.class);
            Privileges privileges = annotation.privileges();
            boolean adminPrivilegesRequired = privileges == Privileges.ADMINISTRATOR;
            boolean userIsAdmin = Boolean.parseBoolean((String) claims.get("admin"));

            unauthorized = adminPrivilegesRequired && !userIsAdmin;

        } catch (JwtException e) {

            unauthorized = true;
        }

        if (unauthorized) {

            requestContext.abortWith(response);
        }
    }
}