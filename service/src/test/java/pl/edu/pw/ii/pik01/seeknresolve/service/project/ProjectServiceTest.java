package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    private ProjectService projectService;

    @Before
    public void setUp() {
        projectService = new ProjectService(projectRepository, new DtosFactory());
    }

    @Test
    public void testTrue() {
        Long simpleId = 1234L;
        when(projectRepository.exists(simpleId)).thenReturn(false);

        try {
            projectService.delete(simpleId);
            failBecauseExceptionWasNotThrown(EntityNotFoundException.class);
        } catch (EntityNotFoundException e) {
        }
    }

    @Test
    public void shouldDeleteExistingBug() {
        Long simpleId = 1234L;
        when(projectRepository.exists(simpleId)).thenReturn(true);

        projectService.delete(simpleId);

        verify(projectRepository, times(1)).delete(simpleId);
    }
}
