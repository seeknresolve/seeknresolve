package org.seeknresolve.test;

import org.seeknresolve.domain.common.test.builders.BugBuilder;
import org.seeknresolve.domain.common.test.builders.ProjectBuilder;
import org.seeknresolve.domain.common.test.builders.UserBuilder;
import org.seeknresolve.domain.entity.*;
import org.seeknresolve.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TestEntityFactory {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private UserProjectRoleRepository userProjectRoleRepository;

    public User createAndSaveUser(String login) {
        User user = new UserBuilder().withLogin(login)
                .withFirstName("R").withLastName("N")
                .withEmail(System.currentTimeMillis() + "r@r.pl").withRole(createAndSaveUserRole("TEST_USER"))
                .build();
        user.setPassword("abcd");
        return userRepository.save(user);
    }

    public UserRole createAndSaveUserRole(String roleName) {
        UserRole userRole = new UserRole(roleName);
        return roleRepository.save(userRole);
    }

    public Project createAndSaveProject(String name, String tag) {
        Project project = new ProjectBuilder().withName(name).withTag(tag).withDescription("Description").build();
        return projectRepository.save(project);
    }

    public Bug createAndSaveBug(String tag, User user, Project project) {
        Bug bug = new BugBuilder().withTag(tag).withName("Name").withDescription("Description").withAssignee(user).withReporter(user).withProject(project).build();
        return bugRepository.save(bug);
    }

    public ProjectRole grantUserProjectRoleWithPermissions(User user, Project project, String roleName, String... permissions) {
        ProjectRole projectRole = createAndSaveProjectRole(roleName, permissions);
        createAndSaveRoleInProject(user, project, projectRole);
        return projectRole;
    }

    private ProjectRole createAndSaveProjectRole(String roleName, String... permissions) {
        ProjectRole projectRole = new ProjectRole(roleName);
        addPermissionsToProjectRole(projectRole, permissions);
        roleRepository.save(projectRole);
        return projectRole;
    }

    private void addPermissionsToProjectRole(ProjectRole projectRole, String... permissions) {
        for(String permissionName : permissions) {
            Permission permission = new Permission(permissionName);
            projectRole.addPermission(permission);
        }
    }

    private void createAndSaveRoleInProject(User user, Project project, ProjectRole projectRole) {
        UserProjectRole userProjectRole = new UserProjectRole(user, project, projectRole);
        userProjectRoleRepository.save(userProjectRole);
    }
}
