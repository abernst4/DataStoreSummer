package datastore.hub_api.route;
import datastore.hub_api.entity.GroupURL;
import datastore.hub_api.repository.HubRepository;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
//import javax.ws.rs.core.Response.Status;
import javax.transaction.Transactional;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import org.jboss.logging.Logger;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.inject.Inject;
import reactor.core.publisher.Mono;



@Path("/hub")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HubRoutes {
    @Inject 
    HubRepository hubRepo; 

    //private Logger logger = Logger.getLogger(HubRoutes.class);

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
    @Path("/post")
    @Transactional
    public Long create(Object[] groupFields,  @Context UriInfo uriInfo) throws JsonProcessingException {
        if(groupFields == null){
            throw new IllegalArgumentException();
        }
        GroupURL group_url = new GroupURL();
            URL url=null;
            try {
                url = new URL(groupFields[0].toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            group_url.url = url;
            group_url.id=(long)((int)groupFields[1]);

        //Recording the groups id and url in db
        this.hubRepo.persist(group_url);
        this.serveURLs();
        //UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        //uriBuilder.path(Long.toString(group_url.id));
        return group_url.id;
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
        Map<Long, URL> groupURLs = this.hubRepo.getGroupUrls();
        for (Long id : groupURLs.keySet()) {
            //Post 
            //logger.info(id);

            URL url = this.hubRepo.getGroupUrls().get(id);
            if(url != null){
            WebClient client = WebClient.create(url.toString());
            client
                .post()
                .uri("updateMap")                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .body(Mono.just(groupURLs), Map.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
                //.accept(MediaType.APPLICATION_JSON)                
                //.contentType(MediaType.APPLICATION_FORM_URLENCODED)                
                //.get().accept(MediaType.APPLICATION_JSON)
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
        URL deleted_url = hubRepo.findById(id).url;
        boolean deleted = hubRepo.deleteById(id);
        //if(deleted==false){
            //throw new IllegalStateException("why is this not delete");
       // }
      // if (!deleted || deleted_url == null) {
            //return Response.status(BAD_REQUEST).build();
       // }
        //Arrays.asList(deleted_url)
          
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
                //.bodyToMono(String.class)
                //.block();
        }    
        
        return Response.noContent().build();
    }
}

