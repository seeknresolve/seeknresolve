package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.ProjectBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.ProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserProjectRoleRepository userProjectRoleRepository;

    private ProjectService projectService;

    @Before
    public void setUp() {
        projectService = new ProjectService(projectRepository, userProjectRoleRepository, new DtosFactory());
    }

    @Test
    public void shouldDeleteExistingBug() {
        Long simpleId = 1234L;
        when(projectRepository.exists(simpleId)).thenReturn(true);

        projectService.delete(simpleId);

        verify(projectRepository, times(1)).delete(simpleId);
    }

    @Test
    public void shouldReturnEmptyListOfProjectsForUserWithNoRoles() {
        //given:
        User user = givenUser("rnw");
        givenNoRolesReturnedForUser(user);
        //when:
        List<ProjectDTO> projects = projectService.getAll(user);
        //then:
        assertThat(projects).isEmpty();
    }

    private User givenUser(String login) {
        User user = new User();
        user.setLogin(login);
        return user;
    }

    private void givenNoRolesReturnedForUser(User user) {
        when(userProjectRoleRepository.findByUser(user)).thenReturn(Lists.newArrayList());
    }

    @Test
    public void shouldReturnProjectForUserWithRoles() {
        //given:
        User user = givenUser("rnw");
        Project project = givenProject(11L, "testowy");
        Project project2 = givenProject(12L, "testowy 2");
        Project project3 = givenProject(13L, "testowy 3");
        givenRolesReturnedForUser(user, project, project2);
        //when:
        List<ProjectDTO> projects = projectService.getAll(user);
        //then:
        assertThat(projects).isNotEmpty();
        assertThat(projects).extracting("id", "name").
                contains(tuple(11L, project.getName()), tuple(12L, project2.getName())).doesNotContain(tuple(13L, project3.getName()));
    }

    private Project givenProject(Long id, String name) {
        return new ProjectBuilder().withId(id).withName(name).build();
    }

    private void givenRolesReturnedForUser(User user, Project... projects) {
        when(userProjectRoleRepository.findByUser(user)).thenReturn(Lists.newArrayList(
                Lists.newArrayList(projects).stream()
                        .map(project -> createUserProjectRole(user, project))
                        .collect(Collectors.toList())
        ));
    }

    private UserProjectRole createUserProjectRole(User user, Project project) {
        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setProject(project);
        userProjectRole.setUser(user);
        ProjectRole projectRole = new ProjectRole();
        userProjectRole.setProjectRole(projectRole);
        return userProjectRole;
    }
}
