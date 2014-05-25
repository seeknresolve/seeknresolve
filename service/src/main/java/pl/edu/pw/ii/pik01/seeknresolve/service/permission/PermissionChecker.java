package pl.edu.pw.ii.pik01.seeknresolve.service.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Permission;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import java.util.Set;

@Component
public class PermissionChecker {
    private final UserService userService;
    private final UserProjectRoleRepository userProjectRoleRepository;

    @Autowired
    public PermissionChecker(UserService userService, UserProjectRoleRepository userProjectRoleRepository) {
        this.userService = userService;
        this.userProjectRoleRepository = userProjectRoleRepository;
    }

    public boolean hasProjectPermission(Project project, Permission permission) {
        //TODO: maybe JPQL would be better solution
        User user = userService.getLoggedUser();
        UserProjectRole userProjectRole = userProjectRoleRepository.findByUserAndProject(user, project);

        if(userProjectRole != null) {
            return hasPermission(userProjectRole.getProjectRole().getPermissions(), permission);
        } else {
            return false;
        }
    }

    public boolean hasPermission(Permission permission) {
        //TODO: consider JPQL
        User user = userService.getLoggedUser();
        return hasPermission(user.getUserRole().getPermissions(), permission);
    }

    private boolean hasPermission(Set<Permission> permissions, Permission permission) {
        return !permissions
                .stream()
                .filter(setPermission -> setPermission.getPermission().equals(permission.getPermission()))
                .findFirst().isPresent();
    }
}
