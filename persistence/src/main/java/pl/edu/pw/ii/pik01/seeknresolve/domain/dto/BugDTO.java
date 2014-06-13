package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BugDTO {
    @NotEmpty
    private String tag;
    @NotEmpty
    private String name;
    private String description;
    private DateTime dateCreated;
    private DateTime dateModified;
    private Long reporterId;
    private String reporterLogin;
    private String reporterName;
    private Long assigneeId;
    private String assigneeLogin;
    private String assigneeName;
    private Long projectId;
    private String projectName;
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

    public String getReporterLogin() {
        return reporterLogin;
    }

    public void setReporterLogin(String reporterLogin) {
        this.reporterLogin = reporterLogin;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeLogin() {
        return assigneeLogin;
    }

    public void setAssigneeLogin(String assigneeLogin) {
        this.assigneeLogin = assigneeLogin;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    @Override
    public String toString() {
        return "BugDTO{" +
                "tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                ", reporterId=" + reporterId +
                ", reporterName='" + reporterName + '\'' +
                ", assigneeId=" + assigneeId +
                ", assigneeName='" + assigneeName + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", state=" + state +
                ", priority=" + priority +
                '}';
    }
}
