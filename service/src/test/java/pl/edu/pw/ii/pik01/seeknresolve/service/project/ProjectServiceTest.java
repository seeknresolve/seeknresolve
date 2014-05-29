package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.ProjectBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.TestWithSecurity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    private TestWithSecurity testWithSecurity = new TestWithSecurity();

    private ProjectService projectService;

    @Before
    public void setup() {
        projectService = new ProjectService(projectRepository, testWithSecurity.userProjectRoleRepository, new DtosFactory());
    }

    @Test
    public void shouldCreateProjectFromDTO() {
        //given:
        given(projectRepository.save(any(Project.class))).willReturn(createProjectForSave(0L, "Test1"));
        ProjectDTO projectToSave = createProjectDTOToSave(0L, "Test1");
        //when:
        ProjectDTO savedProject = projectService.createAndSaveNewProject(projectToSave);
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
        List<ProjectDTO> projects = projectService.getAll(user);
        //then:
        assertThat(projects).isEmpty();
    }

    private User givenUser(String login) {
        User user = new User();
        user.setLogin(login);
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
        List<ProjectDTO> projects = projectService.getAll(user);
        //then:
        assertThat(projects).isNotEmpty();
        assertThat(projects).extracting("id", "name").
                contains(tuple(11L, project.getName()), tuple(12L, project2.getName())).doesNotContain(tuple(13L, project3.getName()));
    }

    private Project givenProject(Long id, String name) {
        return new ProjectBuilder().withId(id).withName(name).build();
    }
}
