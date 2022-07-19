package hub_api.entity;

import java.net.InetAddress;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class GroupStuff extends PanacheEntityBase {
    @Id
    public long groupId;

    public InetAddress ia;

    public GroupStuff(long GroupId, InetAddress ia) {
        this.groupId = GroupId;
        this.ia = ia;
    }

    public GroupStuff(){}

    public Map<Long, InetAddress> toMap() {
        Map<Long, InetAddress> IdMap = new HashMap<>();
        List<GroupStuff> groupList = GroupStuff.listAll();

        for (GroupStuff gs : groupList) {
            IdMap.put(gs.groupId, gs.ia);
        }

        return IdMap;
    }
}
