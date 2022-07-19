package datastore.hub_api.database;

import java.net.InetAddress;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

import io.quarkus.hibernate.orm.panache.

public class HubRepository extends PanacheEntityBase{
    public Map<Long, InetAddress> toMap() {
        Map<Long, InetAddress> IdMap = new HashMap<>();
        List<HubRepository> groupList = HubRepository.listAll();
        for (HubRepository gs : groupList) {
            IdMap.put(gs.groupId, gs.ia);
        }
        return IdMap;
    }
}
