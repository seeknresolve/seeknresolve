package org.seeknresolve.domain.common.test.builders;

import org.joda.time.DateTime;
import org.seeknresolve.domain.entity.Project;

public class ProjectBuilder {
    private Long id = null;
    private String name = "";
    private String description = "";
    private String tag = "TAG";
    private DateTime dateCreated = DateTime.now();
    private DateTime dateModified = DateTime.now();
    private long lastBugNumber = 0;

    public Project build() {
        Project project = new Project();
        project.setId(id);
        project.setName(name);
        project.setTag(tag);
        project.setDescription(description);
        project.setDateCreated(dateCreated);
        project.setDateModified(dateModified);
        project.setLastBugNumber(lastBugNumber);
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

    public ProjectBuilder withTag(String tag) {
        this.tag = tag;
        return this;
    }

    public ProjectBuilder withDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public ProjectBuilder withDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public ProjectBuilder withLastBugNumber(long lastBugNumber) {
        this.lastBugNumber = lastBugNumber;
        return this;
    }
}
