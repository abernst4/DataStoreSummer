package datastore.hub_api.database;
import java.net.URI;
import javax.persistence.Entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class GroupURL extends PanacheEntity{
    public URI uri; 
}