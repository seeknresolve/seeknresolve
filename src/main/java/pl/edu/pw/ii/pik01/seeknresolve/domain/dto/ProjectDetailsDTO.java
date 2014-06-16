package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import org.joda.time.DateTime;

import java.util.List;

public class ProjectDetailsDTO {
    private ProjectDTO project;
    private final List<BugDTO> bugs;
    private List<UserProjectRoleDTO> users;

    public ProjectDetailsDTO(ProjectDTO project, List<BugDTO> bugs, List<UserProjectRoleDTO> users) {
        this.project = project;
        this.bugs = bugs;
        this.users = users;
    }

    public Long getId() {
        return project.getId();
    }

    public String getName() {
        return project.getName();
    }

    public String getDescription() {
        return project.getDescription();
    }

    public DateTime getDateCreated() {
        return project.getDateCreated();
    }

    public List<BugDTO> getBugs() {
        return bugs;
    }

    public List<UserProjectRoleDTO> getUsers() {
        return users;
    }
}
