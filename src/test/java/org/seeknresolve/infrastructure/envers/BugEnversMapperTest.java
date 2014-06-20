package org.seeknresolve.infrastructure.envers;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.test.builders.BugBuilder;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class BugEnversMapperTest {

    private BugEnversMapper bugEnversMapper = new BugEnversMapper();

    @Test
    public void testSimpleDiff() throws IntrospectionException, IllegalAccessException {
        Bug bug = new BugBuilder().withName("bugName").build();
        Bug bugWithChangedName = new BugBuilder().withName("bugName2").build();

        List<RevisionDiffDTO> diffs = bugEnversMapper.diff(buildRevisions(ImmutableList.of(bug, bugWithChangedName)));

        assertThat(diffs.size() == 1);
        assertThat(diffs.get(0).getDescription().contains("Name"));
        assertThat(diffs.get(0).getDescription().contains("bugName"));
        assertThat(diffs.get(0).getDescription().contains("bugName2"));
    }

    @Test
    public void testTripleDiff() throws IntrospectionException, IllegalAccessException {
        Bug bug = new BugBuilder().withName("bugName").build();
        Bug secondBug= new BugBuilder().withName("bugName2").build();
        Bug thirdBug = new BugBuilder().withName("bugName3").build();


        List<RevisionDiffDTO> diffs = bugEnversMapper.diff(buildRevisions(ImmutableList.of(bug, secondBug, thirdBug)));

        assertThat(diffs.size() == 2);
        assertThat(diffs.get(0).getDescription().contains("Name"));
        assertThat(diffs.get(0).getDescription().contains("bugName"));
        assertThat(diffs.get(0).getDescription().contains("bugName2"));

        assertThat(diffs.get(1).getDescription().contains("Name"));
        assertThat(diffs.get(1).getDescription().contains("bugName2"));
        assertThat(diffs.get(1).getDescription().contains("bugName3"));
    }

    @Test
    public void testDiffWitMultipleFields() throws IntrospectionException, IllegalAccessException {
        Bug bug = new BugBuilder().withName("bugName").withState(Bug.State.NEW).withPriority(Bug.Priority.HIGH).build();
        Bug secondBug= new BugBuilder().withName("bugName2").withState(Bug.State.NEW).withPriority(Bug.Priority.CRITICAL).build();
        Bug thirdBug = new BugBuilder().withName("bugName3").withState(Bug.State.IN_PROGRESS).withPriority(Bug.Priority.CRITICAL).build();


        List<RevisionDiffDTO> diffs = bugEnversMapper.diff(buildRevisions(ImmutableList.of(bug, secondBug, thirdBug)));

        assertThat(diffs.size() == 2);
        assertThat(diffs.get(0).getDescription().contains("Name"));
        assertThat(diffs.get(0).getDescription().contains("bugName"));
        assertThat(diffs.get(0).getDescription().contains("bugName2"));
        assertThat(diffs.get(0).getDescription().contains("Priority"));
        assertThat(diffs.get(0).getDescription().contains("High"));
        assertThat(diffs.get(0).getDescription().contains("Critical"));

        assertThat(diffs.get(1).getDescription().contains("Name"));
        assertThat(diffs.get(1).getDescription().contains("bugName2"));
        assertThat(diffs.get(1).getDescription().contains("bugName3"));
        assertThat(diffs.get(1).getDescription().contains("State"));
        assertThat(diffs.get(1).getDescription().contains("New"));
        assertThat(diffs.get(1).getDescription().contains("In progress"));
    }

    private List<Revision<Integer, Bug>> buildRevisions(Iterable<Bug> bugs) {
        List<Revision<Integer, Bug>> revisions = new ArrayList<>();
        bugs.forEach(bug -> revisions.add(new Revision<Integer, Bug>(mock(RevisionMetadata.class), bug)));
        return revisions;
    }
}
