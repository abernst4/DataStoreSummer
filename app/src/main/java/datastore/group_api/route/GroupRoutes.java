package datastore.group_api.route;

import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;
import datastore.group_api.map.GroupURL;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
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
import reactor.core.publisher.Mono;

@Path("/groups/")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupRoutes {
    @Inject 
    GroupRepository groupRepo;
    
    @Inject
    GroupURL groups; 
    
    @ConfigProperty(name = "URL")
    String URL;

    @ConfigProperty(name = "hubURL")
    String hubURL;

    @ConfigProperty(name = "Port")
    int Port; 

    @GET
    @Path("redirect")
    public Response getOnServer() {
        return Response.status(Status.OK).entity(groupRepo.findAll().firstResult()).build();
    }

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
     * @throws URISyntaxException
     */
    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id, @Context UriInfo uriInfo) throws URISyntaxException {
        if (id == null) {
            throw new IllegalArgumentException();
        } 

        Group group = this.groupRepo.findById(id);
        if(group != null){
            return Response.status(Status.OK).entity(group).build();
        }
       
        if (this.groups.group == null ||id != this.groups.group.id) {
            return this.groups.redirect(id, uriInfo);
        }
       
        return Response.status(Status.NOT_FOUND).build();
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
    public Response update(@PathParam("id") Long id, Group updated_group, @Context UriInfo uriInfo) throws URISyntaxException {
        if (id == null || updated_group == null) {
            throw new IllegalArgumentException();
        }     

        if (this.groups.group == null || id != this.groups.group.id) {
            return this.groups.redirect(id, uriInfo);
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
    
    @POST
    @Path("updateMap")
    @Transactional
    public void updateMaps(Map<Long, URL> groupMap){
        if ( groupMap == null) {
            throw new IllegalArgumentException();
        }
        groups.urls = groupMap;
    }

    /**
     * @param group
     * @return
     * @throws UnknownHostException
     * @throws MalformedURLException
     */
    @PUT
    @Path("{id}/URL")
    public Response updateURL(@PathParam("id")Long id, String url){
        Group group = this.groupRepo.findById(id);
        URL url2 = group.url; 
        try {
            url2 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } 
        group.url = url2; 
        return Response.status(Status.OK).build();
    }

    @GET
    @Path("map")
    public Map<Long, URL> getMap(){
        return this.groups.urls;
    }

    /**
     * @param group
     * @param uriInfo
     * @return
     */
    @POST
    @Transactional
     public Response create(Group group, @Context UriInfo uriInfo) {
        groupRepo.persist(group);
        
        this.groups.urls.put(group.id, group.url);
        
        Object[] arr = {group.url, group.id};
        WebClient client = WebClient.create(hubURL); 
        client
            .post()
            .uri("/post")
            .body(Mono.just(arr), Object[].class)
            .retrieve()
            .bodyToMono(Void.class)
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
     * @throws URISyntaxException
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id, @Context UriInfo uriInfo) throws URISyntaxException {
        if (id == null) {
            throw new IllegalArgumentException();
        }  
        
        boolean deleted = groupRepo.deleteById(id);
        
        if (!deleted) {
            return Response.status(BAD_REQUEST).build();
        }
        
        WebClient client = WebClient.create(hubURL);
        client
            .delete()
            .uri("/{id}",id)
            .retrieve() 
            .bodyToMono(Void.class)
            .block();
        return Response.noContent().build();
    }       
}