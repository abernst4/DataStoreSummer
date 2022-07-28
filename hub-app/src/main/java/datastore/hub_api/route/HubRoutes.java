package datastore.hub_api.route;
import datastore.hub_api.entity.GroupURL;
import datastore.hub_api.repository.HubRepository;
import io.quarkus.logging.Log;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.transaction.Transactional;

import java.net.URL;
import java.util.*;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.inject.Inject;
import reactor.core.publisher.Mono;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import org.springframework.http.MediaType;


@Path("/hub")
@Produces(MediaType.APPLICATION_JSON_VALUE)
@Consumes(MediaType.APPLICATION_JSON_VALUE)
public class HubRoutes {
    
    @Inject 
    HubRepository hubRepo; 

    /**
     * @param id
     * @return
     */
    @GET
    public List<GroupURL> getURLS(@QueryParam("id")Long id){
       if(id == null){
        return this.hubRepo.listAll();
       }
       List<GroupURL> list = new ArrayList<>();
       GroupURL url = this.hubRepo.findByIdOptional(id).orElseThrow(NotFoundException::new);
       list.add(url);
       return list; 
    }

    /**
     * 
     * @param group_url
     * @param uriInfo
     * @return group_url.id 
     * @throws JsonProcessingException
     * groupFields contains the fields for the group object just created 
     * index[0] is the group URL
     * index[1] is the group id
     */
    @POST
    //@Transactional
    public Long create(GroupURL groupURL, @Context UriInfo uriInfo) throws JsonProcessingException {
        if(groupURL == null){
            throw new IllegalArgumentException();
        }
        doCreate(groupURL);
        this.serveURLs();
        return groupURL.id;
    }

    
    @Transactional
    public Long doCreate(GroupURL groupURL) throws JsonProcessingException {
        this.hubRepo.persist(groupURL);
        if(groupURL == null){
            throw new IllegalArgumentException();
        }
        return groupURL.id;
    }


    /**
     * WebClient is an interface representing the main entry point for performing web requests.
     * is a non-blocking, reactive client for performing HTTP requests with Reactive Streams back pressure.
     * create - Create a new WebClient with Reactor Netty by default.
     * post - Start building an HTTP POST request
     * uri - Specify the URI starting with a URI template and finishing off with a UriBuilder created from the template.
     * body - We can call .body() with a Flux (including a Mono), which can stream content asynchronously to build the request body.
     * bodyHoldMono - The WebClient class uses reactive features, in the form of a Mono to hold the content of the message
     */
    private void serveURLs() {
        for (GroupURL groupURL : this.hubRepo.listAll()) {
            URL url = groupURL.url;
            try {

                WebClient client = WebClient.create(url.toString());
                client
                    .post()
                    .uri("/groups/updateMap")                
                    .body(Mono.just(hubRepo.getGroupUrls()), Map.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            } catch (WebClientResponseException e) {
                Log.info(e);
            } 
        }
    }

    /**
     * @param group_url
     * @param uriInfo
     * @param id
     * @return
     */
    @PUT
    @Path("/update/{id}")
    @Transactional
    public long update(GroupURL group_url, @Context UriInfo uriInfo ,@PathParam("id") Long id){
        URL url = this.hubRepo.getGroupUrls().get(id);
        if(url == null){
            throw new NotFoundException();
        }
        this.hubRepo.getGroupUrls().put(id, group_url.url);
        this.hubRepo.persist(group_url);
        this.serveURLs();
        return group_url.id; 
    }


    

    /**
     * @throws IllegalArgumentException
     * @param id
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        
        boolean deleted = hubRepo.deleteById(id);
        if (!deleted) {
            return Response.status(BAD_REQUEST).build();
        }
          
        for (URL url : this.hubRepo.getGroupUrls().values()) {
            WebClient client = WebClient.create(url.toString());
            client
                .put()
                .uri("/updateMap")
                .body(
                    Mono.just(hubRepo.getGroupUrls()), 
                    Map.class
                )
                .retrieve();
        }    
        
        return Response.noContent().build();
    }
}

