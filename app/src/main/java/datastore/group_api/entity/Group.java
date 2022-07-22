package datastore.group_api.entity;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import java.net.URL;
import java.util.*;
import datastore.user_api.entity.User;

@Entity
@Table(name = "groups")
public class Group extends PanacheEntity{
    public String name;
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    public List<User> users;
    public URL url; 
    //@ElementCollection()
    //@OneToOne(mappedBy= "group", fetch=FetchType.EAGER)
    @Transient
    transient public Map<Long,URL> urlMap = new HashMap<>(); 

//    public Map<Long, URL> map; 
}


