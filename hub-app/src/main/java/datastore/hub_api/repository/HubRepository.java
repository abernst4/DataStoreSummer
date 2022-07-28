package datastore.hub_api.repository;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import datastore.hub_api.entity.GroupURL;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

/**
 * Converts Java group objects, whihc contian ids and urls, and converts them into a database table
 */
@ApplicationScoped
public class HubRepository implements PanacheRepository<GroupURL> {
    public Map<Long, URL> getGroupUrls() {
        Map<Long, URL> groupMap = new HashMap<>();
        for (GroupURL group_url : listAll()) {
            groupMap.put(group_url.id, group_url.url);
        }
        return groupMap;
    }
}