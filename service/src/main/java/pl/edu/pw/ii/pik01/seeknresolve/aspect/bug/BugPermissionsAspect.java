package pl.edu.pw.ii.pik01.seeknresolve.aspect.bug;

import com.google.common.collect.Lists;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Permission;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.BugRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionChecker;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
public class BugPermissionsAspect {
    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private PermissionChecker permissionChecker;

    @Before("execution(* pl.edu.pw.ii.pik01.seeknresolve.service.bug.BugService.getBugWithTag(..)) && args(tag, ..)")
    public void checkBugViewPermission(JoinPoint joinPoint, String tag) {
        Bug bug = bugRepository.findOne(tag);
        checkPermissionsOnBug(bug, "project:view", "project:everything");
    }

    private void checkPermissionsOnBug(Bug bug, String... permissionsNames) {
        List<Permission> permissions = createPermissions(permissionsNames);
        boolean hasPermission = permissions.stream()
                .filter(permission -> permissionChecker.hasProjectPermission(bug.getProject(), permission))
                .findAny().isPresent();
        if(!hasPermission) {
            throw new SecurityException("No permission to see this bug.");
        }
    }

    private List<Permission> createPermissions(String... permissionsNames) {
        return Lists.newArrayList(permissionsNames).stream()
                .map(permissionName -> new Permission(permissionName)).collect(Collectors.toList());
    }
}
