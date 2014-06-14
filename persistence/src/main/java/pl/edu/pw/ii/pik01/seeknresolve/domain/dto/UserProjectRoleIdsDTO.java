package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

public class UserProjectRoleIdsDTO {
    String role;
    Long UserId;
    Long ProjectId;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Long getProjectId() {
        return ProjectId;
    }

    public void setProjectId(Long projectId) {
        ProjectId = projectId;
    }
}
