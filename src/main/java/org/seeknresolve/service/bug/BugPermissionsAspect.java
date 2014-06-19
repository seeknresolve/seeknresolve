package org.seeknresolve.service.bug;

import com.google.common.collect.Lists;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.seeknresolve.domain.dto.BugDTO;
import org.seeknresolve.domain.dto.CommentDTO;
import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.entity.Permission;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.repository.BugRepository;
import org.seeknresolve.domain.repository.ProjectRepository;
import org.seeknresolve.service.permission.PermissionChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Aspect
public class BugPermissionsAspect {
    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PermissionChecker permissionChecker;

    @Before("execution(* org.seeknresolve.service.bug.BugService.createAndSaveNewBug(..)) && args(bugDTO, ..)")
    public void checkBugCreatePermission(JoinPoint joinPoint, BugDTO bugDTO) {
        Project project = getProject(bugDTO.getProjectId());
        checkPermissionsOnProject(project, "project:everything", "project:add_bug");
    }

    private Project getProject(Long projectId) {
        Project project = projectRepository.findOne(projectId);
        validateObjectExists(project, projectId);
        return project;
    }

    private void checkPermissionsOnProject(Project project, String... permissionsNames) {
        List<Permission> permissions = getPermissionsWithNames(permissionsNames);
        hasAnyOfPermissionsOnProject(project, permissions);
    }

    private void hasAnyOfPermissionsOnProject(Project project, List<Permission> permissions) {
        boolean hasPermission = permissions.stream()
                .filter(permission -> permissionChecker.hasProjectPermission(project, permission))
                .findAny().isPresent();
        if(!hasPermission) {
            throw new SecurityException("No permission to perform operation on bug.");
        }
    }

    @Before("execution(* org.seeknresolve.service.bug.BugService.getBugWithTag(..)) && args(tag, ..)")
    public void checkBugViewPermission(JoinPoint joinPoint, String tag) {
        Bug bug = getBug(tag);
        checkPermissionsOnBug(bug, "project:view", "project:everything");
    }

    private Bug getBug(String tag) {
        Bug bug = bugRepository.findOne(tag);
        validateObjectExists(bug, tag);
        return bug;
    }

    private <T, ID> void validateObjectExists(T object, ID id) {
        if(object == null) {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }

    private void checkPermissionsOnBug(Bug bug, String... permissionsNames) {
        List<Permission> permissions = getPermissionsWithNames(permissionsNames);
        Project project = bug.getProject();
        hasAnyOfPermissionsOnProject(project, permissions);
    }

    private List<Permission> getPermissionsWithNames(String... permissionsNames) {
        return Lists.newArrayList(permissionsNames).stream()
                .map(permissionName -> new Permission(permissionName)).collect(Collectors.toList());
    }

    @Before("execution(* org.seeknresolve.service.bug.BugService.deleteBugWithTag(..)) && args(tag, ..)")
    public void checkBugDeletePermission(JoinPoint joinPoint, String tag) {
        Bug bugToDelete = getBug(tag);
        checkPermissionsOnBug(bugToDelete, "project:view", "project:everything", "project:delete");
    }

    @Before("execution(* org.seeknresolve.service.comment.CommentService.createAndSaveNewComment(..)) && args(commentDTO, ..)")
    public void checkCommentPermission(JoinPoint joinPoint, CommentDTO commentDTO) {
        Bug bug = getBug(commentDTO.getBugTag());
        checkPermissionsOnBug(bug, "project:view", "project:everything");
    }

    @Before("execution(* org.seeknresolve.service.bug.BugService.updateBug(..)) && args(bugDTO, ..)")
    public void checkBugEditPermission(JoinPoint joinPoint, BugDTO bugDTO) {
        Bug bug = getBug(bugDTO.getTag());
        checkPermissionsOnBug(bug, "project:view", "project:everything", "project:edit_bug");
    }
}
