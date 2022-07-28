package datastore.hub_api.entity;

import java.net.URL;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;


@Entity
public class GroupURL extends PanacheEntity { 
    public URL url; 
}