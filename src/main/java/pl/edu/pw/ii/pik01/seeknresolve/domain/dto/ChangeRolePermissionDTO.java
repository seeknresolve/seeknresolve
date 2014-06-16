package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

public class ChangeRolePermissionDTO {
    private String roleName;
    private String permissionName;

    public ChangeRolePermissionDTO() {
    }

    public ChangeRolePermissionDTO(String roleName, String permissionName) {
        this.roleName = roleName;
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
