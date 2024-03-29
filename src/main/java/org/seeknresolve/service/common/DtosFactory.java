package org.seeknresolve.service.common;

import org.seeknresolve.domain.dto.*;
import org.seeknresolve.domain.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class DtosFactory {
    private DtosFactory() {
    }

    public static ProjectDTO createProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setTag(project.getTag());
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
        bugDTO.setReporterLogin(bug.getReporter().getLogin());
        bugDTO.setReporterName(String.join(" ", bug.getReporter().getFirstName(), bug.getReporter().getLastName()));
        if(bug.getAssignee() != null) {
            bugDTO.setAssigneeId(bug.getAssignee().getId());
            bugDTO.setAssigneeLogin(bug.getAssignee().getLogin());
            bugDTO.setAssigneeName(String.join(" ", bug.getAssignee().getFirstName(), bug.getAssignee().getLastName()));
        }
        return bugDTO;
    }

    public static List<BugDTO> createBugDTOList(List<Bug> bugList) {
        return bugList.stream()
                .map(DtosFactory::createBugDTO)
                .collect(Collectors.toList());
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


    public static UserDetailsDTO createUserDetailsDTO(User user) {
        UserDTO userDto = createUserDTO(user);
        List<BugDTO> reportedBugs = createBugDTOList(user.getReportedBugs());
        List<BugDTO> assignedBugs = createBugDTOList(user.getAssignedBugs());
        return new UserDetailsDTO(userDto, reportedBugs, assignedBugs);
    }

    public static List<UserDTO> createUserDTOList(List<User> userList) {
        return userList.stream()
                .map(user -> createUserDTO(user))
                .collect(Collectors.toList());
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

    public static UserProjectRoleDTO createProjectUserDTO(UserProjectRole userProjectRole) {
        return new UserProjectRoleDTO(DtosFactory.createUserDTO(userProjectRole.getUser()), DtosFactory.createRoleDTO(userProjectRole.getProjectRole()));
    }

    public static ProjectDetailsDTO createProjectDetailsDTO(List<UserProjectRoleDTO> users, List<BugDTO> bugDTOs, ProjectDTO projectDTO) {
        return new ProjectDetailsDTO(projectDTO, bugDTOs, users);
    }
}
