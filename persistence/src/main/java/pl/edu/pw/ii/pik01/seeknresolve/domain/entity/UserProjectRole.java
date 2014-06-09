package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class UserProjectRole {

    @Embeddable
    public static class UserProjectId implements Serializable {
        private long userId;
        private long projectId;
    }

    @EmbeddedId
    private UserProjectId id;

    @MapsId("userId")
    @ManyToOne(optional = false)
    private User user;

    @MapsId("projectId")
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

    public UserProjectId getId() {
        return id;
    }

    public void setId(UserProjectId id) {
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

        if (!project.equals(that.project)) return false;
        if (!projectRole.equals(that.projectRole)) return false;
        if (!user.equals(that.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 * user.hashCode();
        result = 31 * result + project.hashCode();
        result = 31 * result + projectRole.hashCode();
        return result;
    }
}
