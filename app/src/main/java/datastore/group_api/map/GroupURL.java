package datastore.group_api.map;

import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;

import java.net.*;
import java.util.*;

import javax.inject.Singleton;

import javax.ws.rs.core.*;

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
            return Response.temporaryRedirect(new URI (this.urls.get(id).toString() + uriInfo.getPath())).build();
        }
        return Response.status(NOT_FOUND).build();
    }

    /**
     * @param id
     * @param uriInfo
     * @param name
     * @return
     * @throws URISyntaxException
     */
    public Response redirect (long id, @Context UriInfo uriInfo, String name) throws URISyntaxException {
        if (urls.keySet().contains(id)) {
            return Response.temporaryRedirect(new URI (this.urls.get(id).toString() + uriInfo.getPath()+
            "?group-id="+id+"&name="+name))
                           .build();
        }
        return Response.status(NOT_FOUND).build();
    }
}