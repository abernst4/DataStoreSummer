package com.geekcap.javaworld.jpa.repository;
import java.util.List;
import  com.geekcap.javaworld.jpa.resource.GroupResource;
import  com.geekcap.javaworld.jpa.model.Group;
import com.geekcap.javaworld.jpa.model.User;

import javax.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class GroupRepository implements PanacheRepository<Group> {
    public Group findByName(String name) {
        return find("name", name).firstResult();//it needs to have .firstResult()
    }

    public Group findByUser(User user){
        return find("users", user).firstResult();
    }
}
/*
import com.geekcap.javaworld.jpa.model.Group; //THE BUG IS IN MY PACKAGING; SINCE I SHOULDN'T HAVE TO USE MAIN/JAVA
import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;

//I THINK THAT THERE SHOULD BE A FIELD FOR THE HASHSET WHEN WE ARE DOING MANY TO MANY RELATIONSHIP
//I have to add this to the pomXMl
//Ask him about why he used to different methods for QuerySearch. 
//ask yoel about syntax for QuerySearch; also ask yoel about if the repository class should only use look up by 
//using the pimary key
import javax.persistence.EntityManager;
public class GroupRepository{
    private EntityManager manager;
    public GroupRepository(EntityManager manager){
        this.manager = manager; 
    }

    public Optional<Group> findById(Integer Id){ //I noticed that you had to use the class Integer and not int
        Group group = manager.find(Group.class, Id);
        return group != null? Optional.of(group): Optional.empty();
    }

    public Optional<Group> findByName(String name){
        Group group = (Group) manager.createQuery("SELECT group FROM Group group WHERE group.id = :id", Group.class)
            .setParameter("name", name)
            .getSingleResult();
            return name != null? Optional.of(group): Optional.empty();
    }

    public Optional<Group> findNameByNameQuery(String name){
        Group group = manager.createNamedQuery("group.findbyname", Group.class)
            .setParameter("name",name)
            .getSingleResult();
        return group != null? Optional.of(group):Optional.empty();
    }

    public Optional<Group> save(Group group){
        try{
            this.manager.getTransaction().begin();
            this.manager.persist(group);
            this.manager.getTransaction().commit();
            return Optional.of(group);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Set<Group> findAll(){
        return (Set<Group>) manager.createQuery("from Group").getResultList();
    }

    public void deleteById(Integer id){
        com.geekcap.javaworld.jpa.model.Group group = this.manager.find(Group.class, id);
        if(group != null){
            try{
                manager.getTransaction().begin();
                group.getUsers().forEach(User -> User.getGroups().remove(group));
                manager.remove(group);
                manager.getTransaction().commit();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
*/