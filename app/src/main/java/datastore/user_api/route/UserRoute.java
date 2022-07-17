package datastore.user_api.route;
import datastore.user_api.entity.User;
import datastore.user_api.database.UserRepository;
import datastore.group_api.entity.Group;
import datastore.group_api.database.GroupRepository;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import javax.transaction.Transactional;

@Path("/userID")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRoute {

    @Inject UserRepository userRepo;
    @Inject GroupRepository groupRepo;

    @GET
    @Path("/{group-id}/users")
    public List<User> getAll(
                            @PathParam("group-id") long groupId,
                            @QueryParam("name") String name,
                            @QueryParam("email") String email) {
        if (name == null & email == null) {
            return groupRepo.findByIdOptional(groupId).orElseThrow(NotFoundException::new).users;
        } else if (email == null) {
            return userRepo.findByName(groupId, name);
        } else if (name == null) {
            return userRepo.findByEmail(groupId, email);
        } else {
            return userRepo.findByNameAndEmail(groupId, name, email);
        }
    }
    

    @GET
    @Path("/{id}")
    public User getById(@PathParam("id") Long id) {
        return userRepo.findById(id);
    }
    
    @POST
    @Transactional
    @Path("/{group-id}/users")
    public Response create(@PathParam("group-id") long groupId, User user, @Context UriInfo uriInfo) {
        Group group = groupRepo.findByIdOptional(groupId).orElseThrow(NotFoundException::new);
        user.group = group;
        userRepo.persist(user);
        if (!userRepo.isPersistent(user)) {
            throw new NotFoundException();
        }
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Long.toString(user.id));
        return Response.created(uriBuilder.build()).entity(user).status(Status.CREATED).build();
    }
    
    @PUT
    @Path("/{group-id}/users/{id}")
    @Transactional
    public Response update(@PathParam("group-id") Long groupId, @PathParam("id") Long id, User user) {
        groupRepo.findByIdOptional(groupId).orElseThrow(NotFoundException::new);
        User entity = userRepo.findById(id);
        if (entity == null) {
            return Response.status(NOT_FOUND).build();
        }
        entity.name = user.name;
        entity.email = user.email;
        return Response.status(Status.OK).entity(entity).build();
    }

    @DELETE
    @Path("/{group-id}/users/{id}")
    @Transactional
    public Response deleteById(@PathParam("group-id") Long groupId, @PathParam("id") Long id) {
        groupRepo.findByIdOptional(groupId).orElseThrow(NotFoundException::new);
        boolean deleted = userRepo.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(BAD_REQUEST).build();
    }
}