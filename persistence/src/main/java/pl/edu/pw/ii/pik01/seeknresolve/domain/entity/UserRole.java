package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import javax.persistence.Entity;

@Entity
public class UserRole extends Role {

    public UserRole(){}

    public UserRole(String roleName){
        super(roleName);
    }

}
