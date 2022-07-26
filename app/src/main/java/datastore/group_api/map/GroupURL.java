package datastore.group_api.map;

import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.springframework.web.reactive.function.client.WebClient;

import com.google.inject.Inject;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Singleton
public class GroupURL {
    public Group group;
    public Map<Long, URL> urls = new HashMap<>();

    @Inject
    GroupRepository groupRepo;

    public Response redirect (long id, @Context UriInfo uriInfo) throws URISyntaxException {
        if (urls.keySet().contains(id)) {
            return Response.temporaryRedirect(new URI (urls.get(id).toString() + uriInfo.getPath())).build();
        }
        return Response.status(NOT_FOUND).build();
    }
    
    /**
     * @param group_id
     * @param uriInfo
     * @param httpMethod
     * @param body
     * @param returnEntities
     * @return
     * @throws URISyntaxException
     * The URI must contain the string "batch" in place of the groupId!
     */
    public Response redirect(long [] groups, @Context UriInfo uriInfo) throws URISyntaxException {
        List<Object> returnValues = new ArrayList<>();

        for (long group_id : groups) {
            WebClient client = WebClient.create(this.urls.get(group_id).toString());
            String path = uriInfo.getPath();
            path = path.replace("batch", Long.toString(group_id));
            Object response = client
                                    .get()
                                    .uri(path)
                                    .retrieve()
                                    .bodyToMono(Object.class)
                                    .block();
            if (response instanceof Collection) {
                returnValues.addAll((Collection)response);
            }
            else {
                returnValues.add(response);
            }
        }
        return Response.status(Status.OK).entity(returnValues).build();
    }
}