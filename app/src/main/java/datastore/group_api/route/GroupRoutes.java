package datastore.group_api.route;
import java.net.MalformedURLException;
//import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

//import javax.resource.spi.ConfigProperty;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import org.springframework.web.reactive.function.client.WebClient;
import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;

//import reactor.core.publisher.Mono;

@Path("/groups/")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupRoutes {

    @Inject 
    GroupRepository groupRepo;

    @ConfigProperty(name = "URL")
    String URL;

    @ConfigProperty(name = "hubURL")
    String hubURL;

    @ConfigProperty(name = "Port")
    int Port; 
    /**
     * @param name
     * @return
     */
    @GET
    public List<Group> getAll(@QueryParam("name") String name) {
        if(name == null){
            return groupRepo.listAll();
        }
        return groupRepo.findByName(name);
    }

    /**
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    public Group getById(@PathParam("id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }            
        return groupRepo.findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    /**
     * @param id
     * @param gr
     * @return
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, Group updated_group) {
        if (id == null || updated_group == null) {
            throw new IllegalArgumentException();
        }        
        Group group = groupRepo.findById(id);
        if (group == null) {
            throw new NotFoundException();
        }
        group.name = updated_group.name;
        groupRepo.persist(group);
        return Response .status(Status.OK)
                        .entity(group)
                        .build();
    }
    
    
    /**
     * @param group
     * @return
     * @throws UnknownHostException
     * @throws MalformedURLException
     */
    @POST
    @Transactional
     public Response create(Group group, @Context UriInfo uriInfo) {
        groupRepo.persist(group);
        
        //Add web client logic and incorperate the hubUrl from application.properties
        //Made change

        //String hubURL = ConfigProvider.getConfig().getValue("hubURL", String.class);
        //Add web client logic and incorperate the hubUrl from application.properties
        WebClient client = WebClient.create("http://localhost:8081"); //localhost:8081
        group.id = client
                        .post()
                        .uri("/hub/post")
                        //.contentType(())
                        .bodyValue(group.url)//{url"\"" + group.url + "}
                        .header("Authorization", "Bearer MY_SECRET_TOKEN")
                        .retrieve()
                        .bodyToMono(Long.class)
                        .block();
        
        if (groupRepo.isPersistent(group)) {
            return Response
                            .created(uriInfo
                                .getAbsolutePathBuilder()
                                .path(Long.toString(group.id))
                                .build()
                            )
                            .entity(group)
                            .status(Status.CREATED)
                            .build();
        }
        return Response.status(NOT_FOUND).build();
    } 

    /**
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }        
        boolean deleted = groupRepo.deleteById(id);
        
        if (!deleted) {
            return Response.status(BAD_REQUEST).build();
        }
        WebClient client = WebClient.create("http://localhost:8081"); //hubURL
        client.delete()
                //.post()
                .uri("/hub/" + id)
                .retrieve()           
                .bodyToMono(Long.class)
                .block();
        return Response.noContent().build();
    }  

    /**
     * 
     */
     
}