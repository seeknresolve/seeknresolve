package pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders;

import org.joda.time.DateTime;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;

public class BugBuilder {
    private String tag;
    private String name = "";
    private String description = "";
    private DateTime dateCreated = DateTime.now();
    private DateTime dateModified = DateTime.now();
    private User reporter = new User();
    private User assignee = new User();
    private Project project = new Project();
    private Bug.State state = Bug.State.NEW;
    private Bug.Priority priority = Bug.Priority.NORMAL;

    public Bug build() {
        Bug bug = new Bug();
        bug.setTag(tag);
        bug.setName(name);
        bug.setDescription(description);
        bug.setDateCreated(dateCreated);
        bug.setDateModified(dateModified);
        bug.setReporter(reporter);
        bug.setAssignee(assignee);
        bug.setProject(project);
        bug.setState(state);
        bug.setPriority(priority);
        return bug;
    }

    public BugBuilder withTag(String tag) {
        this.tag = tag;
        return this;
    }

    public BugBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BugBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public BugBuilder withDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public BugBuilder withDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public BugBuilder withReporter(User reporter) {
        this.reporter = reporter;
        return this;
    }

    public BugBuilder withAssignee(User assigne) {
        this.assignee = assigne;
        return this;
    }

    public BugBuilder withProject(Project project) {
        this.project = project;
        return this;
    }

    public BugBuilder withState(Bug.State state) {
        this.state = state;
        return this;
    }

    public BugBuilder withPriority(Bug.Priority priority) {
        this.priority = priority;
        return this;
    }
}
