package org.seeknresolve.service.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.dto.ProjectDTO;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.entity.ProjectRole;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserProjectRole;
import org.seeknresolve.domain.repository.BugRepository;
import org.seeknresolve.domain.repository.ProjectRepository;
import org.seeknresolve.domain.repository.UserRepository;
import org.seeknresolve.domain.test.builders.ProjectBuilder;
import org.seeknresolve.service.common.TestWithSecurity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BugRepository bugRepository;

    private TestWithSecurity testWithSecurity = new TestWithSecurity();

    private ProjectService projectService;

    @Before
    public void setup() {
        projectService = new ProjectService(projectRepository, userRepository, bugRepository,
                testWithSecurity.userProjectRoleRepository, testWithSecurity.roleRepository);
    }

    @Test
    public void shouldCreateProjectFromDTO() {
        //given:
        given(projectRepository.save(any(Project.class))).willReturn(createProjectForSave(0L, "Test1"));
        ProjectDTO projectToSave = createProjectDTOToSave(0L, "Test1");
        User user = givenUser("rnw");
        //when:
        ProjectDTO savedProject = projectService.createAndSaveNewProject(projectToSave, user);
        //then:
        assertThat(savedProject).isNotNull();
        assertThat(savedProject.getId()).isEqualTo(0L);
        assertThat(savedProject.getName()).isEqualTo("Test1");
        verifyProjectWasSavedInDb(0L, "Test1");
    }

    private Project createProjectForSave(Long id, String name) {
        return new ProjectBuilder().withId(id).withName(name).build();
    }

    private ProjectDTO createProjectDTOToSave(Long id, String name) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(id);
        projectDTO.setName(name);
        return projectDTO;
    }

    private void verifyProjectWasSavedInDb(Long id, String name) {
        ArgumentCaptor<Project> projectArgument = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository, times(1)).save(projectArgument.capture());
        assertThat(projectArgument.getValue().getId()).isEqualTo(id);
        assertThat(projectArgument.getValue().getName()).isEqualTo(name);
    }

    @Test
    public void shouldGrantUserPMRoleWhenHeCreatesNewProject() {
        //given:
        Project projectToSave = createProjectForSave(0L, "Test1");
        ProjectRole projectRole = createProjectRoleForTest("PM");
        given(projectRepository.save(any(Project.class))).willReturn(projectToSave);
        given(testWithSecurity.roleRepository.findOne(any(String.class))).willReturn(projectRole);
        ProjectDTO projectDTOToSave = createProjectDTOToSave(0L, "Test1");
        User user = givenUser("rnw");
        //when:
        ProjectDTO savedProject = projectService.createAndSaveNewProject(projectDTOToSave, user);
        //then:
        assertThat(savedProject).isNotNull();
        verifyRoleWasGranted(user, projectToSave, projectRole);
    }

    private ProjectRole createProjectRoleForTest(String roleName) {
        return new ProjectRole(roleName);
    }

    private void verifyRoleWasGranted(User user, Project project, ProjectRole projectRole) {
        UserProjectRole userProjectRole = new UserProjectRole(user, project, projectRole);
        verify(testWithSecurity.userProjectRoleRepository, times(1)).save(userProjectRole);
    }

    @Test
    public void shouldDeleteExistingProject() {
        Long simpleId = 1234L;
        given(projectRepository.exists(simpleId)).willReturn(true);

        projectService.delete(simpleId);

        verify(projectRepository, times(1)).delete(simpleId);
    }

    @Test
    public void shouldReturnEmptyListOfProjectsForUserWithNoRoles() {
        //given:
        User user = givenUser("rnw");
        testWithSecurity.givenNoRolesReturnedForUser(user);
        //when:
        List<ProjectDTO> projects = projectService.getAllPermittedProjects(user);
        //then:
        assertThat(projects).isEmpty();
    }

    private User givenUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setId(666L);
        return user;
    }

    @Test
    public void shouldReturnProjectForUserWithRoles() {
        //given:
        User user = givenUser("rnw");
        Project project = givenProject(11L, "testowy");
        Project project2 = givenProject(12L, "testowy 2");
        Project project3 = givenProject(13L, "testowy 3");
        testWithSecurity.givenRolesReturnedForUser(user, project, project2);
        //when:
        List<ProjectDTO> projects = projectService.getAllPermittedProjects(user);
        //then:
        assertThat(projects).isNotEmpty();
        assertThat(projects).extracting("id", "name").
                contains(tuple(11L, project.getName()), tuple(12L, project2.getName())).doesNotContain(tuple(13L, project3.getName()));
    }

    private Project givenProject(Long id, String name) {
        return new ProjectBuilder().withId(id).withName(name).build();
    }
}
