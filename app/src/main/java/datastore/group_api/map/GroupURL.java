package datastore.group_api.map;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import datastore.group_api.entity.Group;

public class GroupURL{
    public Group group;
    public Map<Long, URL> urls = new HashMap<>();
}