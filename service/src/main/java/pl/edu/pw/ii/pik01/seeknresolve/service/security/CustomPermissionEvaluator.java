package pl.edu.pw.ii.pik01.seeknresolve.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Permission;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionChecker;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

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

        Permission permission = new Permission();
        permission.setPermission(permissionText.toString());

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

        checkNotNull(project);

        Permission permission = new Permission();
        permission.setPermission(permissionText.toString());

        return permissionChecker.hasProjectPermission(project, permission);
    }
}
