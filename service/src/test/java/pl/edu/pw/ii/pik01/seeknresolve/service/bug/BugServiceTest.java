package pl.edu.pw.ii.pik01.seeknresolve.service.bug;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.BugRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BugServiceTest {
    @Mock
    private BugRepository bugRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    private BugService bugService;

    @Before
    public void setup() {
        bugService = new BugService(bugRepository, projectRepository, userRepository, new DtosFactory());
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
}
