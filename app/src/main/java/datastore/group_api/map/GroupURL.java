package datastore.group_api.map;

import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Singleton
public class GroupURL {
    public Group group;
    public Map<Long, URL> urls = new HashMap<>();

    @Inject
    GroupRepository groupRepo;

    /**
     * @param id
     * @param uriInfo
     * @return
     * @throws URISyntaxException
     */
    public Response redirect (long id, @Context UriInfo uriInfo) throws URISyntaxException {
        if (urls.keySet().contains(id)) {
            return Response.temporaryRedirect(new URI (this.urls.get(id).toString() + Long.toString(id)))
                        .build();
        }
        return Response.status(NOT_FOUND).build();
    }
}