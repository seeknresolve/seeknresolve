package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import java.util.Set;

public class RoleDTO {
    private String roleName;
    private Set<PermissionDTO> permissions;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
