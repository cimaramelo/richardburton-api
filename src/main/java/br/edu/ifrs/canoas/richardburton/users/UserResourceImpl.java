package br.edu.ifrs.canoas.richardburton.users;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Stateless
public class UserResourceImpl implements UserResource {

    @Inject
    private UserService userService;

    public Response create(User user) {

        try {

            user = userService.create(user);
            return Response.status(Response.Status.CREATED).entity(user).build();

        } catch (EmailNotUniqueException e) {

            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();

        } catch (ConstraintViolationException e) {

            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    public Response retrieveAll() {

        List<User> users = userService.retrieveAll();
        return Response.status(Response.Status.OK).entity(users).build();
    }

    public Response retrieve(Long id) {

        User user = userService.retrieve(id);

        if (user != null) {

            return Response.ok(user).build();

        } else {

            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}