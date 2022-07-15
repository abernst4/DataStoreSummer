package datastore.group_api.entity;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*;
import datastore.user_api.entity.User;

@Entity
@Table(name = "groups")
public class Group extends PanacheEntity{
    public String name;
    @OneToMany(mappedBy = "group")
    public List<User> users;
}


