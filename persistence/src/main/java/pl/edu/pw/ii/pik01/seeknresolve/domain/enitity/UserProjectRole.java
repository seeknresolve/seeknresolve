package pl.edu.pw.ii.pik01.seeknresolve.domain.enitity;

import javax.persistence.*;

@Entity
public class UserProjectRole {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Project project;

    @ManyToOne(optional = false)
    private ProjectRole projectRole;

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
}
