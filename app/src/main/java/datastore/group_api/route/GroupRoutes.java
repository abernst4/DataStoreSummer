package datastore.group_api.route;

//import datastore.group_api.route.Server;
import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;
//import datastore.group_api.entity.GroupURLs;
import datastore.group_api.map.GroupURL;

//import java.lang.ProcessBuilder.Redirect;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


//import reactor.core.publisher.Mono;

@Path("/groups/")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupRoutes {
    @Inject 
    GroupRepository groupRepo;

    //@Inject 
    //GroupURL group_urls;
    
    //@Inject 
    //Server server;
    @Inject
    GroupURL groups; 
    
    @ConfigProperty(name = "URL")
    String URL;

    @ConfigProperty(name = "hubURL")
    String hubURL;

    @ConfigProperty(name = "Port")
    int Port; 

    
    //Map<Long, URL> groupURLs = new HashMap<>(); 
    
   @GET
    public Response getOnServer() {
        return Response.status(Status.OK).entity(groupRepo.findAll().firstResult()).build();
   }

    //@GET
    //@Path("redirect")
    //public Response getOnServer(@QueryParam("groups") long[] groups, @Context UriInfo uriInfo) throws URISyntaxException {
        //return group_urls.redirect(groups, uriInfo);
    //}

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
    public Response getById(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        if (id == null) {
            throw new IllegalArgumentException();
        } 
        Group group = this.groupRepo.findById(id);
       if(group != null){
        return Response.status(Status.FOUND).entity(group).build();
       }
       
       //URL url = this.groups.urls.get(id);
       //WebClient client = WebClient.create(url.toString());
       //Group g2 = client.get().uri("{id}/", id).retrieve().bodyToMono(Group.class).block();
       //return g2; 
      // group_urls.group == null ||
        //if (id != group.group.id) {
            //return groups.redirect(id, uriInfo);
        //}
        URL url = groups.urls.get(id);
            if(url!= null){
               WebClient client = WebClient.create(url.toString());
       Group g2 = client.get().uri("{id}/", id).retrieve().bodyToMono(Group.class).block();
       return Response.status(Status.TEMPORARY_REDIRECT).entity(g2).build(); 
            }
        

            return Response.status(Status.NOT_FOUND).build();
        //return groupRepo.findByIdOptional(id).orElseThrow(NotFoundException::new);
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
        //group_urls.group == null ||   
        //if (group_urls.group == null || id != group_urls.group.id) {
            //return group_urls.redirect(id, uriInfo);
       //}
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
    @Path("{id}/updateMap")
    @Transactional
    public void updateMaps(@PathParam("id") Long id,  Map<Long, URL> groupMap){
        if ( groupMap == null) {
            throw new IllegalArgumentException();
        }
        //groupMap.forEach((k,v)-> this.groups.urls.put(k, v));        
        groups.urls = groupMap;
        //return Response.status(Status.OK).build();
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
            // TODO Auto-generated catch block
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
        
        //groups.group = group;
        this.groups.urls.put(group.id, group.url);
        //Add web client logic and incorperate the hubUrl from application.properties
        //Made change
        //String hubURL = ConfigProvider.getConfig().getValue("hubURL", String.class);
        //Add web client logic and incorperate the hubUrl from application.properties
        Object[] arr = {group.url, group.id};

        WebClient client = WebClient.create(hubURL); 
         client
                        .post()
                        .uri("/post")
                        .body(Mono.just(arr), Object[].class)
                        //.contentType(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();
                        //.bodyValue()//{url"\"" + group.url + "}
                        //.header("Authorization", "Bearer MY_SECRET_TOKEN")
        
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
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }  
        //this.groups.urls.remove(id);
        
        //if (group_urls.group == null || id != group_urls.group.id) {
            //return group_urls.redirect(id, uriInfo);
       // }
        
        boolean deleted = groupRepo.deleteById(id);
        
        //groups.group = null;

        if (!deleted) {
            return Response.status(BAD_REQUEST).build();
        }
        
        WebClient client = WebClient.create(hubURL);
        client//.method(HttpMethod.DELETE)
        .delete()
        .uri("/{id}",id)
        //.body(Mono.just(id), Long.class)
        .retrieve() 
       .bodyToMono(Void.class).block();
        //.toEntity(Void.class);
        
        
        
        //delete()
                //.post()
               // .uri("/hub/{id}",id);
                //.retrieve();           
                //.bodyToMono(Long.class)
                //.block();
        return Response.noContent().build();
    }  

    /**
     * 
     */
     
}