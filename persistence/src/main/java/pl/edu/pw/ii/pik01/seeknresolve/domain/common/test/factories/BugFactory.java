package pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.factories;

import org.joda.time.DateTime;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.User;

public class BugFactory {
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

    public BugFactory withTag(String tag) {
        this.tag = tag;
        return this;
    }

    public BugFactory withName(String name) {
        this.name = name;
        return this;
    }

    public BugFactory withDescription(String description) {
        this.description = description;
        return this;
    }

    public BugFactory withDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public BugFactory withDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public BugFactory withReporter(User reporter) {
        this.reporter = reporter;
        return this;
    }

    public BugFactory withAssignee(User assigne) {
        this.assignee = assigne;
        return this;
    }

    public BugFactory withProject(Project project) {
        this.project = project;
        return this;
    }

    public BugFactory withState(Bug.State state) {
        this.state = state;
        return this;
    }

    public BugFactory withPriority(Bug.Priority priority) {
        this.priority = priority;
        return this;
    }
}
