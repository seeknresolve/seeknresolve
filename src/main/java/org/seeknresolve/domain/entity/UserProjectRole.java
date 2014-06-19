package org.seeknresolve.domain.entity;

import javax.persistence.*;

@Entity
public class UserProjectRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne(optional = false)
    private ProjectRole projectRole;

    public UserProjectRole() {
    }

    public UserProjectRole(User user, Project project, ProjectRole projectRole) {
        this.user = user;
        this.project = project;
        this.projectRole = projectRole;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectRole getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProjectRole that = (UserProjectRole) o;

        if (id != that.id) return false;
        if (!project.equals(that.project)) return false;
        if (!projectRole.equals(that.projectRole)) return false;
        if (!user.equals(that.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + user.hashCode();
        result = 31 * result + project.hashCode();
        result = 31 * result + projectRole.hashCode();
        return result;
    }
}
