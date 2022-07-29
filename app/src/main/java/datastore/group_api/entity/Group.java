package datastore.group_api.entity;

import datastore.user_api.entity.User;

import javax.persistence.Id;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.net.URL;

import java.util.*;

@javax.persistence.Entity
@javax.persistence.Table(name = "groups")
public class Group extends PanacheEntityBase {
    @Id
    public long id; 
    public String name;

    public URL url; 

    @OneToMany(mappedBy = "group")
    public List<User> users;
}



