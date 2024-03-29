package org.seeknresolve.service.bug;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.dto.BugDTO;
import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.repository.BugRepository;
import org.seeknresolve.domain.repository.CommentRepository;
import org.seeknresolve.domain.repository.ProjectRepository;
import org.seeknresolve.domain.repository.UserRepository;
import org.seeknresolve.domain.test.builders.BugBuilder;
import org.seeknresolve.domain.test.builders.ProjectBuilder;
import org.seeknresolve.service.bug.generator.BugTagGenerator;
import org.seeknresolve.service.common.DtosFactory;
import org.seeknresolve.service.common.TestWithSecurity;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BugServiceTest {
    @Mock
    private BugRepository bugRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BugTagGenerator bugTagGenerator;

    private TestWithSecurity testWithSecurity = new TestWithSecurity();

    private BugService bugService;

    @Before
    public void setup() {
        bugService = new BugService(bugRepository, projectRepository,
                userRepository, commentRepository,
                testWithSecurity.userProjectRoleRepository, bugTagGenerator);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionIfBugToDeleteDoesNotExist() {
        //given:
        String sampleTag = "BUG-123";
        given(bugRepository.exists(sampleTag)).willReturn(false);
        //when:
        bugService.deleteBugWithTag(sampleTag);
    }

    @Test(expected = Test.None.class)
    public void shouldDeleteExistingBug() {
        //given:
        String sampleTag = "BUG-123";
        given(bugRepository.exists(sampleTag)).willReturn(true);
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

        Project project = givenProject(11L, "testowy", "T1");
        project.addBug(givenBug("T1", "Test1", Bug.State.NEW));
        project.addBug(givenBug("T2", "Test2", Bug.State.NEW));

        Project project2 = givenProject(12L, "testowy 2", "T2");
        project2.addBug(givenBug("T3", "Test3", Bug.State.NEW));

        Project project3 = givenProject(13L, "testowy 3", "T3");
        project3.addBug(givenBug("T4", "Test4", Bug.State.NEW));

        testWithSecurity.givenRolesReturnedForUser(user, project, project2);
        //when:
        List<BugDTO> bugs = bugService.getAllPermittedBugs(user);
        //then:
        assertThat(bugs).isNotNull();
        assertThat(bugs).isNotEmpty();
        assertThat(bugs).extracting("tag", "name").contains(tuple("T1", "Test1"), tuple("T2", "Test2"), tuple("T3", "Test3"))
                .doesNotContain(tuple("T4", "Test4"));
    }

    private Project givenProject(Long id, String name, String tag) {
        return new ProjectBuilder().withId(id).withName(name).withTag(tag).build();
    }

    private Bug givenBug(String tag, String name, Bug.State state) {
        return new BugBuilder().withTag(tag).withName(name).withState(state).build();
    }

    @Test
    public void shouldUpdateBugWithOnlyChangedFields() {
        //given:
        ArgumentCaptor<Bug> bugArgumentCaptor = ArgumentCaptor.forClass(Bug.class);
        Bug bug = givenBug("TTT-3", "Testowy3", Bug.State.READY_TO_TEST);
        BugDTO updateData = givenUpdateData(bug, "Nowa nazwa");
        given(bugRepository.findOne("TTT-3")).willReturn(bug);
        given(bugRepository.save(bugArgumentCaptor.capture())).willReturn(createBugFromDTO(updateData));
        //when:
        bugService.updateBug(updateData);
        //then:
        Bug updatedBug = bugArgumentCaptor.getValue();
        assertThat(updatedBug).isNotNull();
        assertThat(updatedBug.getName()).isEqualTo("Nowa nazwa");
        assertThat(updatedBug.getState()).isEqualTo(Bug.State.READY_TO_TEST);
    }

    private BugDTO givenUpdateData(Bug bug, String name) {
        BugDTO bugDTO = DtosFactory.createBugDTO(bug);
        bugDTO.setName(name);
        return bugDTO;
    }

    private Bug createBugFromDTO(BugDTO bugDTO) {
        return new BugBuilder().withName(bugDTO.getName()).build();
    }
}
