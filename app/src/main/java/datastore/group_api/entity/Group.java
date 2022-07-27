package datastore.group_api.entity;

import datastore.user_api.entity.User;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.net.URL;

import java.util.*;

@javax.persistence.Entity
@javax.persistence.Table(name = "groups")
public class Group extends PanacheEntity {
    public String name;
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    public List<User> users;
    public URL url; 
}