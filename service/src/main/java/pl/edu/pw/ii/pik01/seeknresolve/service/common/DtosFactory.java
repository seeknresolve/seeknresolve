package pl.edu.pw.ii.pik01.seeknresolve.service.common;

import org.springframework.stereotype.Component;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtosFactory {

    private DtosFactory() {
    }

    public static ProjectDTO createProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setDateCreated(project.getDateCreated());
        return projectDTO;
    }

    public static BugDTO createBugDTO(Bug bug) {
        BugDTO bugDTO = new BugDTO();
        bugDTO.setDateCreated(bug.getDateCreated());
        bugDTO.setDateModified(bug.getDateModified());
        bugDTO.setTag(bug.getTag());
        bugDTO.setName(bug.getName());
        bugDTO.setDescription(bug.getDescription());
        bugDTO.setState(bug.getState());
        bugDTO.setPriority(bug.getPriority());
        bugDTO.setProjectId(bug.getProject().getId());
        bugDTO.setProjectName(bug.getProject().getName());
        bugDTO.setReporterId(bug.getReporter().getId());
        bugDTO.setReporterName(String.join(" ", bug.getReporter().getFirstName(), bug.getReporter().getLastName()));
        if(bug.getAssignee() != null) {
            bugDTO.setAssigneeId(bug.getAssignee().getId());
            bugDTO.setAssigneeName(String.join(" ", bug.getAssignee().getFirstName(), bug.getAssignee().getLastName()));
        }
        return bugDTO;
    }

    public static BugDetailsDTO createBugDetailsDTO(BugDTO bug, List<CommentDTO> comments) {
        return new BugDetailsDTO(bug, comments);
    }

    public static UserDTO createUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserRole(user.getUserRole().getRoleName());
        return userDTO;
    }

    public static RoleDTO createRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(role.getRoleName());
        if(role.getPermissions() != null) {
            roleDTO.setPermissions(role
                    .getPermissions()
                    .stream()
                    .map(permission -> createPermissionDTO(permission))
                    .collect(Collectors.toSet()));
        }
        return roleDTO;
    }

    public static PermissionDTO createPermissionDTO(Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName(permission.getPermissionName());
        return permissionDTO;
    }

    public static CommentDTO createCommentDTO(Comment comment, String authorLogin) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setDateCreated(comment.getDateCreated());
        commentDTO.setContent(comment.getContent());
        commentDTO.setAuthorLogin(authorLogin);
        commentDTO.setBugTag(comment.getBug().getTag());
        return commentDTO;
    }
}
