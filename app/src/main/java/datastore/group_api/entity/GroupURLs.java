/* 
package datastore.group_api.entity;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@javax.persistence.Table(name = "group_urls")
public class GroupURLs extends PanacheEntityBase{
    //@CollectionTable(name = "order_item_mapping", 
    //joinColumns = {@JoinColumn(name = "url", referencedColumnName = "id")})
    //@MapKeyColumn(name = "urlMap")
   
    //@ElementCollection()
    @OneToOne(mappedBy="group", fetch=FetchType.EAGER)
    public Map<Long,URL> urlMap = new HashMap<>(); 
    public Group group;

}
*/