package datastore.group_api.database;
import datastore.group_api.entity.Group;
import datastore.user_api.entity.User;
import javax.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class GroupRepository implements PanacheRepository<Group> {
    public Group findByName(String name) {
        return find("name", name).firstResult();
    }

    public Group findByUser(User user){
        return find("users", user).firstResult();
    }
}