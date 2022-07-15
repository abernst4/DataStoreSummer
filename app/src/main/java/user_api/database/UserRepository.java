package user_api;

import user_api.entity.User;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>{
    public List<User> findAllByGroup(String group){
       return list("group", group);
   }

    public List<User> findByName(long groupId, String name) {
        return list("group_id = ?1 and name = ?2", groupId, name);
    }

    public List<User> findByEmail(long groupId, String email){
        return list("group_id = ?1 and email = ?2", groupId, email);
    }

    public List<User> findByNameAndEmail(long groupId, String name, String email) {
        return list("group_id = ?1 and name = ?2 and email =?3", groupId, name, email);
    }
}