package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import javax.persistence.*;

@Entity
public class Permission {

    @Id
    private String permissionName;

    public Permission() {}

    public Permission(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (!permissionName.equals(that.permissionName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return 31 * permissionName.hashCode();
    }
}
