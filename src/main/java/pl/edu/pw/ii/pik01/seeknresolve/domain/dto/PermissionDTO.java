package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

public class PermissionDTO {
    private String permissionName;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    @Override
    public String toString() {
        return "PermissionDTO{" +
                "permissionName='" + permissionName + '\'' +
                "}";
    }
}
