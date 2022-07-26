 
package datastore.group_api.map;

import java.net.URL;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.Map;

//import javax.enterprise.inject.Model;
import javax.inject.Singleton;

import com.google.inject.Inject;
//import com.google.inject.Singleton;

import datastore.group_api.database.GroupRepository;
import datastore.group_api.entity.Group;

//@Model
@Singleton
public class GroupURL {
    //public Group group;
    public Map<Long, URL> urls = new HashMap<>();

    //@Inject
    //GroupRepository groupRepo;

}
