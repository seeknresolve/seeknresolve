package org.seeknresolve.service.security;

import org.seeknresolve.domain.entity.Permission;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.repository.ProjectRepository;
import org.seeknresolve.service.permission.PermissionChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    public static final String PROJECT_PERMISSION_TYPE = "Project";

    private final ProjectRepository projectRepository;
    private final PermissionChecker permissionChecker;

    @Autowired
    public CustomPermissionEvaluator(ProjectRepository projectRepository, PermissionChecker permissionChecker) {
        this.projectRepository = projectRepository;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permissionText) {
        checkNotNull(permissionText);

        Permission permission = new Permission(permissionText.toString());

        if(targetDomainObject != null) {
            checkArgument(targetDomainObject instanceof Project, "Permissions are allowed only for Project objects");
            return permissionChecker.hasProjectPermission((Project) targetDomainObject, permission);
        }

        return permissionChecker.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permissionText) {
        checkArgument(targetType.equals(PROJECT_PERMISSION_TYPE), "Only project and user specific permission check is allowed");
        checkNotNull(permissionText);

        Project project = projectRepository.findOne(Long.parseLong(targetId.toString()));

        if(project == null) {
            return false;
        }

        Permission permission = new Permission();
        permission.setPermissionName(permissionText.toString());

        return permissionChecker.hasProjectPermission(project, permission);
    }
}
