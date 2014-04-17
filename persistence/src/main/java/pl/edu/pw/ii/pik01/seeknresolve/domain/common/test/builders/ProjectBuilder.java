package pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders;

import org.joda.time.DateTime;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Project;

public class ProjectBuilder {
    private Long id = 0L;
    private String name = "";
    private String description = "";
    private DateTime dateCreated = DateTime.now();

    public Project build() {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setDescription(description);
        project.setDateCreated(dateCreated);
        return project;
    }

    public ProjectBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProjectBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProjectBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProjectBuilder withDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
}
