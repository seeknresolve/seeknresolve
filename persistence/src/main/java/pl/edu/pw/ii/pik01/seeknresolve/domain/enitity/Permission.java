package pl.edu.pw.ii.pik01.seeknresolve.domain.enitity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Permission {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String permission;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
