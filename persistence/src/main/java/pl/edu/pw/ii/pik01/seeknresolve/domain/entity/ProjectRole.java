package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import javax.persistence.Entity;

@Entity
public class ProjectRole extends Role {

    public ProjectRole(){}

    public ProjectRole(String roleName){
        super(roleName);
    }
}
