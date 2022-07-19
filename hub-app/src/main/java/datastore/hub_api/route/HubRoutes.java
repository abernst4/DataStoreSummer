package datastore.hub_api.route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.persistence.Entity;

import javax.transaction.Transactional;

import java.net.URL;

import java.util.*;

import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import reactor.core.publisher.Mono;

@Path("/hub")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HubRoutes {

    /**
     * Holds group ids and urls
     */
    @Entity
    public class GroupURL extends PanacheEntity{
        public URL url; 
    }    
        
    private HubRepository hubRepo = new HubRepository();


    /**
     * 
     * @param group_url
     * @param uriInfo
     * @return group_url.id 
     * @throws JsonProcessingException
     */
    @POST
    @Transactional
    public long create(GroupURL group_url, @Context UriInfo uriInfo) throws JsonProcessingException {
        //Recording the groups id and url in db
        this.hubRepo.persist(group_url);
        this.serveURLs();

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Long.toString(group_url.id));
        
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
        for (long id : groupURLs.keySet()) {
            //Post 
            URL url = this.hubRepo.getGroupUrls().get(id);
            WebClient client = WebClient.create(url.toString());
            client
                .post()
                .uri("/servers")
                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .body(Mono.just(groupURLs), Map.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
                //.accept(MediaType.APPLICATION_JSON)                
                //.contentType(MediaType.APPLICATION_FORM_URLENCODED)                
                //.get().accept(MediaType.APPLICATION_JSON)
            }
            //change
    }

    /**
     * Converts Java group objects, whihc contian ids and urls, and converts them into a database table
     */
    @ApplicationScoped
    public class HubRepository implements PanacheRepository<GroupURL> {
        public Map<Long, URL> getGroupUrls() {
            Map<Long, URL> IdMap = new HashMap<>();
            //listAll lists all of the url Paneche entities
            for (GroupURL group_url : listAll()) {
                IdMap.put(group_url.id, group_url.url);
            }
            return IdMap;
        }
    }

    /**
     * What is this method doing?
     * @param otherURLs
     */
    /*
    private void recordOtherURLs(List<URL> otherURLs) {
        this.recordURL();
        for (URL url : otherURLs) {
            WebClient client = WebClient.create(url.toString());
            client
                .post()
                .uri("")
                .body(
                    Mono.just(hubRepo.getGroupUrls()), 
                    Map.class
                )
                .retrieve()
                .bodyToMono(String.class).block();
        }
        return;
    }
     */


}

