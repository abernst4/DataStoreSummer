package datastore.hub_api.entity;

import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.Id;

//import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class GroupURL extends PanacheEntityBase{
    @Id
    public Long id; 
    public URL url; 
    public GroupURL(){}
    public GroupURL(URL url, Long id){
    this.url=url;
    this.id= id; 
   }
}   