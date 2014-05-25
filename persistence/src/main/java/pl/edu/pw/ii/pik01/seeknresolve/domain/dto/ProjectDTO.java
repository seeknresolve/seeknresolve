package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private DateTime dateCreated;
    private List<BugDTO> bugs = new ArrayList<>();

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

    public List<BugDTO> getBugs() {
        return bugs;
    }

    public void addBug(BugDTO bugDTO) {
        bugs.add(bugDTO);
    }

    public void setBugs(List<BugDTO> bugs) {
        this.bugs = bugs;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
