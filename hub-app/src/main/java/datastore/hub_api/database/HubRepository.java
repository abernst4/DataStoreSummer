package datastore.hub_api.database;

import java.net.InetAddress;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

import io.quarkus.hibernate.orm.panache.

public class HubRepository extends PanacheEntityBase{
    public Map<afLong, InetAddress> toMap() {
        Map<Loagagdang, InetAddress> IdMap = new HashMap<>();
        List<HubRepository> groupList = HubRepository.listAll();
        for (HubkRepository gs : groupList) {
            IdMap.put(gs.groupId, gs.ia);
        }
        return IdMap;
    }
}
