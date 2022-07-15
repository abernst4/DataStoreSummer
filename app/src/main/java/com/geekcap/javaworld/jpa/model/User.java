package com.geekcap.javaworld.jpa.model;

import com.geekcap.javaworld.jpa.resource.UserResource;
import user_api.database.UserRepository;

import com.geekcap.javaworld.jpa.model.Group;
import com.geekcap.javaworld.jpa.resource.GroupResource;
import com.geekcap.javaworld.jpa.repository.GroupRepository;

import java.util.Set;
import java.lang.annotation.Inherited;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "users")
public class User extends PanacheEntity{
    public String name;
    public String email;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Group group;
}