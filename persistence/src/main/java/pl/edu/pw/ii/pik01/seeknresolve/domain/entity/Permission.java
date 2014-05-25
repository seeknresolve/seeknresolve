package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import javax.persistence.*;

@Entity
public class Permission {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String permission;

    public Permission() {

    }

    public Permission(String permission) {
        this.permission = permission;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (id != that.id) return false;
        if (!permission.equals(that.permission)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + permission.hashCode();
        return result;
    }
}
