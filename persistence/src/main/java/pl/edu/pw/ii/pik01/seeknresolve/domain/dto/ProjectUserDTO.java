package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

public class ProjectUserDTO {
    private UserDTO user;
    private RoleDTO projectRole;

    public ProjectUserDTO(UserDTO user, RoleDTO projectRole) {
        this.user = user;
        this.projectRole = projectRole;
    }

    public Long getId() {
        return user.getId();
    }

    public String getLogin() {
        return user.getLogin();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getUserRole() {
        return user.getUserRole();
    }

    public RoleDTO getProjectRole() {
        return projectRole;
    }
}
