package datastore.group_api.database;
import datastore.group_api.entity.Group;
import datastore.user_api.entity.User;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class GroupRepository implements PanacheRepository<Group> {
    public List<Group> findByName(String name) {
        //return find("name", name).firstResult();
        return list("groups = ?1", name);
    }

    public Group findByUser(User user){
        return find("users", user).firstResult();
    }
}