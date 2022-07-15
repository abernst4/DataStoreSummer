package com.geekcap.javaworld.jpa.model;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
//import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.util.*;


@Entity
@Table(name = "groups")
public class Group extends PanacheEntity{
    public String name;
    @OneToMany(mappedBy = "group")//, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    public List<User> users;
}


