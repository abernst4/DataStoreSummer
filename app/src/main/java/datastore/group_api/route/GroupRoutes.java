package datastore.group_api.route;
import java.util.List;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupRoutes {
    @Inject GroupRepository groupRepo;
    @GET
    public List<Group> getAll(@PathParam("name") String name) {
        return groupRepo.listAll();
    }

    @GET
    @Path("/{id}")
    public Group getById(@PathParam("id") Long id) {
        return groupRepo.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }
    
    @GET
    @Path("/{name}")
    public Group getByName(@PathParam("name") String name) {
        return groupRepo.findByName(name);
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, Group gr) {
        Group group = groupRepo.findById(id);
        if (group == null) {
            throw new NotFoundException();
        }
        group.name = gr.name;
        groupRepo.persist(group);
        return Response.status(Status.OK).entity(group).build();
    }
    
    @POST
    @Path("/create")
    @Transactional
     public Response create(Group group) {
        groupRepo.persist(group);
        if (groupRepo.isPersistent(group)) {
            return Response.status(Status.CREATED).entity(group).build();
        }
        return Response.status(NOT_FOUND).build();
    } 

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        boolean deleted = groupRepo.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(BAD_REQUEST).build();
    }  
}