package org.seeknresolve.domain.dto;

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
