package org.seeknresolve.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import org.joda.time.DateTime;
import org.seeknresolve.domain.entity.Bug;

import java.util.List;

public class BugDetailsDTO {
    private BugDTO bug;
    private List<CommentDTO> comments;

    public BugDetailsDTO() {

    }

    public BugDetailsDTO(BugDTO bug, List<CommentDTO> comments) {
        this.bug = bug;
        this.comments = comments;
    }

    public BugDTO getBug() {
        return bug;
    }

    public void setBug(BugDTO bug) {
        this.bug = bug;
    }

    public String getTag() {
        return bug.getTag();
    }

    public void setTag(String tag) {
        bug.setTag(tag);
    }

    public String getName() {
        return bug.getName();
    }

    public void setName(String name) {
        bug.setName(name);
    }

    public String getDescription() {
        return bug.getDescription();
    }

    public void setDescription(String description) {
        bug.setDescription(description);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getDateCreated() {
        return bug.getDateCreated();
    }

    public void setDateCreated(DateTime dateCreated) {
        bug.setDateCreated(dateCreated);
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getDateModified() {
        return bug.getDateModified();
    }

    public void setDateModified(DateTime dateModified) {
        bug.setDateModified(dateModified);
    }

    public Long getReporterId() {
        return bug.getReporterId();
    }

    public void setReporterId(Long reporterId) {
        bug.setReporterId(reporterId);
    }

    public String getReporterLogin() {
        return bug.getReporterLogin();
    }

    public void setReporterLogin(String reporterLogin) {
        bug.setReporterLogin(reporterLogin);
    }

    public String getReporterName() {
        return bug.getReporterName();
    }

    public void setReporterName(String reporterName) {
        bug.setReporterName(reporterName);
    }

    public Long getAssigneeId() {
        return bug.getAssigneeId();
    }

    public void setAssigneeId(Long assigneeId) {
        bug.setAssigneeId(assigneeId);
    }

    public String getAssigneeLogin() {
        return bug.getAssigneeLogin();
    }

    public void setAssigneeLogin(String assigneeLogin) {
        bug.setAssigneeLogin(assigneeLogin);
    }

    public String getAssigneeName() {
        return bug.getAssigneeName();
    }

    public void setAssigneeName(String assigneeName) {
        bug.setAssigneeName(assigneeName);
    }

    public Long getProjectId() {
        return bug.getProjectId();
    }

    public void setProjectId(Long projectId) {
        bug.setProjectId(projectId);
    }

    public String getProjectName() {
        return bug.getProjectName();
    }

    public void setProjectName(String projectName) {
        bug.setProjectName(projectName);
    }

    public Bug.State getState() {
        return bug.getState();
    }

    public void setState(Bug.State state) {
        bug.setState(state);
    }

    public Bug.Priority getPriority() {
        return bug.getPriority();
    }

    public void setPriority(Bug.Priority priority) {
        bug.setPriority(priority);
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }
}
