package datastore.group_api.route;

import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;
import datastore.group_api.map.GroupURL;

import java.net.*;
import java.util.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.transaction.Transactional;

import javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.*;

import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/groups/")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
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

    @GET
    @Path("map")
    public Map<Long, URL> getMap(){
        return this.groups.urls;
    }

    /**
     * @param group
     * @param uriInfo
     * @return
     * @throws MalformedURLException
     */
    @POST
    @Transactional
     public Response create(Group group, @Context UriInfo uriInfo) throws MalformedURLException, UnknownHostException { 
        if (this.groups.group != null) {
            return Response.status(409, "Server is already associated a group").build();
        }

        this.groups.group = group;

        group.url = new URL(URL);
        
        group.id = connectToHub(group.url);

        groupRepo.persist(group);
        if (groupRepo.isPersistent(group) == false) { 
            throw new NotFoundException();
        }

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Long.toString(group.id));

        return Response.created(uriBuilder.build())
                        .entity(group)
                        .status(Status.CREATED)
                        .build();
    } 

    /**
     * @param group_url
     * @return
     */
    private Long connectToHub(URL group_url) {
        WebClient client = WebClient.create(hubURL); 
        Long new_group_id = client
                                .post()
                                .uri("/hub")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{\"url\":\"" + group_url + "\"}")
                                .retrieve()
                                .bodyToMono(Long.class).block();     
        return new_group_id;
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
        
        if (this.groups.group == null || id != this.groups.group.id) {
            return this.groups.redirect(id, uriInfo);
        }

        boolean deleted = groupRepo.deleteById(id);
        
        if (!deleted) {
            return Response.status(BAD_REQUEST).build();
        }
        this.groups.group = null;
        WebClient client = WebClient.create(hubURL);
        client
            .delete()
            .uri("/hub/{id}",id)
            .retrieve() 
            .bodyToMono(Void.class)
            .block();
        return Response.noContent().build();
    }       
}