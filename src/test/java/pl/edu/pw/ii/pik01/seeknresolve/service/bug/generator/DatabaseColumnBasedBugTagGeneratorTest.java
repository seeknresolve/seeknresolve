package pl.edu.pw.ii.pik01.seeknresolve.service.bug.generator;

import org.junit.Test;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.ProjectBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseColumnBasedBugTagGeneratorTest {
    private DatabaseColumnBasedBugTagGenerator tagGenerator = new DatabaseColumnBasedBugTagGenerator();

    @Test
    public void shouldGenerateNextTagForBug() {
        //given:
        Project project = createProjectWithLastTagSet("TPR", 16L);
        //when:
        String newTag = tagGenerator.generateTag(project);
        //then:
        assertThat(newTag).isNotNull();
        assertThat(newTag).isEqualTo("TPR-17");
    }

    private Project givenProject(long id, String name, String tag) {
        return new ProjectBuilder().withId(id).withName(name).withTag(tag).build();
    }

    private Project createProjectWithLastTagSet(String tag, long lastBugNumber) {
        Project project = givenProject(11L, "testowy", tag);
        project.setLastBugNumber(lastBugNumber);
        return project;
    }
}