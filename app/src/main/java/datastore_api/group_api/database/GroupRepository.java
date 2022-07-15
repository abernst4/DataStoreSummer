package datastore_api.group_api.database;
import  datastore_api.group_api.entity.Group;
import datastore_api.user_api.entity.User;
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