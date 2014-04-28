package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import org.joda.time.DateTime;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Bug;

public class BugDTO {
    private String tag;
    private String name;
    private String description;
    private DateTime dateCreated;
    private DateTime dateModified;
    private Long reporterId;
    private Long assigneeId;
    private Long projectId;
    private Bug.State state;
    private Bug.Priority priority;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public DateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Bug.State getState() {
        return state;
    }

    public void setState(Bug.State state) {
        this.state = state;
    }

    public Bug.Priority getPriority() {
        return priority;
    }

    public void setPriority(Bug.Priority priority) {
        this.priority = priority;
    }
}
