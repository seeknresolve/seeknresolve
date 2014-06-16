package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.pw.ii.pik01.seeknresolve.TestAppContext;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.ProjectBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.UserBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.RolesConstants;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestAppContext.class)
public class ProjectServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        createAndSaveProjectRole(RolesConstants.DEVELOPER_ROLE_NAME);
    }

    @Test
    public void shouldReturnUsersNotAssignedToProject() {
        //given:
        User assignedUser = createAndSaveUser("assignedUser");
        User notAssignedUser = createAndSaveUser("notAssignedUser");
        Project project = createAndSaveProject("projectTest" + System.currentTimeMillis());

        //when:
        projectService.grantRoleForUserToProject(RolesConstants.DEVELOPER_ROLE_NAME, assignedUser.getId(), project.getId());

        //then
        List<UserDTO> notAssigned = userService.findNotAssignedToProject(project.getId());
        assertThat(notAssigned).contains(DtosFactory.createUserDTO(notAssignedUser));
    }

    @Test
    public void shouldRevokeUserRoleFromProject() {
        //given:
        User assigned = createAndSaveUser("assigned");
        User revoked = createAndSaveUser("revoked");
        Project project = createAndSaveProject("projectTest" + System.currentTimeMillis());

        UserProjectRole assignedProjectRole= projectService.grantRoleForUserToProject(RolesConstants.DEVELOPER_ROLE_NAME, assigned.getId(), project.getId());
        UserProjectRole revockedProjectRole = projectService.grantRoleForUserToProject(RolesConstants.DEVELOPER_ROLE_NAME, revoked.getId(), project.getId());

        projectService.revokeRoleFromUserToProject(revoked.getId(), project.getId());

        assertThat(userService.findNotAssignedToProject(project.getId())).contains(DtosFactory.createUserDTO(revoked));
        assertThat(userService.findNotAssignedToProject(project.getId())).doesNotContain(DtosFactory.createUserDTO(assigned));
        assertThat(userService.getAllUserWithRolesOnProject(project.getId())).contains(DtosFactory.createUserDTO(assigned));
        assertThat(userService.getAllUserWithRolesOnProject(project.getId())).doesNotContain(DtosFactory.createUserDTO(revoked));
    }

    private User createAndSaveUser(String login) {
        User user = new UserBuilder().withLogin(login)
                .withFirstName("R").withLastName("N")
                .withEmail("r@r.pl").withRole(createAndSaveUserRole("TEST_USER"))
                .build();
        user.setPassword("abcd");
        return userRepository.save(user);
    }

    private UserRole createAndSaveUserRole(String roleName) {
        UserRole userRole = new UserRole(roleName);
        return roleRepository.save(userRole);
    }

    private ProjectRole createAndSaveProjectRole(String roleName) {
        ProjectRole projectRole = new ProjectRole(roleName);
        return roleRepository.save(projectRole);
    }

    private Project createAndSaveProject(String name) {
        Project project = new ProjectBuilder().withName(name).withTag(name).withDescription("Description").build();
        return projectRepository.save(project);
    }
}
