package org.seeknresolve.domain.entity;

import javax.persistence.Entity;

@Entity
public class UserRole extends Role {

    public UserRole() { }

    public UserRole(String roleName){
        super(roleName);
    }

}
