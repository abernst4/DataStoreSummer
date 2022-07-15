package com.geekcap.javaworld.jpa.model;

import com.geekcap.javaworld.jpa.resource.UserResource;
import com.geekcap.javaworld.jpa.repository.UserRepository;

import com.geekcap.javaworld.jpa.model.Group;
import com.geekcap.javaworld.jpa.resource.GroupResource;
import com.geekcap.javaworld.jpa.repository.GroupRepository;

import java.util.Set;
import java.lang.annotation.Inherited;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.annotation.processing.Generated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "users")
public class User extends PanacheEntity{
    public String name;
    public String email;
    //private Long id; 

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //@JoinColumn(name = "group_id")
    public Group group;
}