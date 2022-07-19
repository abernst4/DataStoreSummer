package datastore.hub_api.route;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.transaction.Transactional;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.*;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.springframework.web.reactive.function.client.WebClient;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URI;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;


@Path("/hub")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HubRoutes {

    @Entity
    public class GroupURL extends PanacheEntity{
        public URL url; 
    }    
        
    private HubRepository hubRepo = new HubRepository();


    @POST
    @Transactional
    public long create(GroupURL group_url, @Context UriInfo uriInfo) throws JsonProcessingException {
        //Recording the groups id and url in db
        this.hubRepo.persist(group_url);
        this.recordURL();

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Long.toString(group_url.id));
        
        return group_url.id;
    }

    private void recordURL() {
        Map<Long, URL> groupURLs = this.hubRepo.getGroupUrls();
        for (long id : groupURLs.keySet()) {
            //Post 
            (this.hubRepo.toMap().get(id));
        }
    }
    

    //This method enables the URL map to be sent to URLs beyond those in the current URL map. 
    //Current use-case is a DELETE, where the URL is deleted from the map, but the gallery located at that URL should still receive that updated map so that it can properly redirect requests. So, this method is called with otherURLs containing the URL of the deleted gallery.
    //Future use-cases for this method might include exporting the URL map to backup servers, or doing a mass delete where more than one URL is deleted from the map and the map needs to be sent to many URLs which are now not present in the map.
    private void recordURL(List<URL> otherURLs) {
        this.recordURL();
        for (URL url : otherURLs) {
            post(url);
        }
    }

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

}

