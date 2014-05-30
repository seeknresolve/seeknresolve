package pl.edu.pw.ii.pik01.seeknresolve.service.bug;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.BugBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.ProjectBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.BugRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.TestWithSecurity;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BugServiceTest {
    @Mock
    private BugRepository bugRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    private TestWithSecurity testWithSecurity = new TestWithSecurity();

    private BugService bugService;

    @Before
    public void setup() {
        bugService = new BugService(bugRepository, projectRepository,
                userRepository, testWithSecurity.userProjectRoleRepository);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionIfBugToDeleteDoesNotExist() {
        //given:
        String sampleTag = "BUG-123";
        when(bugRepository.exists(sampleTag)).thenReturn(false);
        //when:
        bugService.deleteBugWithTag(sampleTag);
    }

    @Test(expected = Test.None.class)
    public void shouldDeleteExistingBug() {
        //given:
        String sampleTag = "BUG-123";
        when(bugRepository.exists(sampleTag)).thenReturn(true);
        //when:
        bugService.deleteBugWithTag(sampleTag);
        //then:
        verify(bugRepository, times(1)).delete(sampleTag);
    }

    @Test
    public void shouldShowNoBugsIfUserHasNoRolesOnAnyProject() {
        //given:
        User user = givenUser("rnw");
        testWithSecurity.givenNoRolesReturnedForUser(user);
        //when:
        List<BugDTO> bugs = bugService.getAllPermittedBugs(user);
        //then;
        assertThat(bugs).isNotNull();
        assertThat(bugs).isEmpty();
    }

    private User givenUser(String login) {
        User user = new User();
        user.setLogin(login);
        return user;
    }

    @Test
    public void shouldReturnBugsFromProjectsOnWhichUserHasAnyRole() {
        //given:
        User user = givenUser("rnw");

        Project project = givenProject(11L, "testowy");
        project.addBug(givenBug("T1", "Test1"));
        project.addBug(givenBug("T2", "Test2"));

        Project project2 = givenProject(12L, "testowy 2");
        project2.addBug(givenBug("T3", "Test3"));

        Project project3 = givenProject(13L, "testowy 3");
        project3.addBug(givenBug("T4", "Test4"));

        testWithSecurity.givenRolesReturnedForUser(user, project, project2);
        //when:
        List<BugDTO> bugs = bugService.getAllPermittedBugs(user);
        //then:
        assertThat(bugs).isNotNull();
        assertThat(bugs).isNotEmpty();
        assertThat(bugs).extracting("tag", "name").contains(tuple("T1", "Test1"), tuple("T2", "Test2"), tuple("T3", "Test3"))
                .doesNotContain(tuple("T4", "Test4"));
    }

    private Project givenProject(Long id, String name) {
        return new ProjectBuilder().withId(id).withName(name).build();
    }

    private Bug givenBug(String tag, String name) {
        return new BugBuilder().withTag(tag).withName(name).build();
    }
}
