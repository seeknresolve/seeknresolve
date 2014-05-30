package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import com.google.common.collect.ImmutableList;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateCreated;

    @OneToMany(mappedBy = "project")
    private List<Bug> bugs = new ArrayList<>();

    public void update(Project updatedProject) {
        setName(updatedProject.getName());
        setDescription(updatedProject.getDescription());
        // TODO: data tworzenia czy update?
        // setDateCreated(updatedProject.getDateCreated());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Bug> getBugs() {
        return ImmutableList.copyOf(bugs);
    }

    public void addBug(Bug bug) {
        bugs.add(bug);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (bugs != null ? !bugs.equals(project.bugs) : project.bugs != null) return false;
        if (dateCreated != null ? !dateCreated.equals(project.dateCreated) : project.dateCreated != null) return false;
        if (description != null ? !description.equals(project.description) : project.description != null) return false;
        if (id != null ? !id.equals(project.id) : project.id != null) return false;
        if (name != null ? !name.equals(project.name) : project.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (bugs != null ? bugs.hashCode() : 0);
        return result;
    }
}
