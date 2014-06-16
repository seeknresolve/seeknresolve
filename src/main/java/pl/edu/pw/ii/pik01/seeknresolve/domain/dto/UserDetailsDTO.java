package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsDTO {
    UserDTO userDTO;
    List<BugDTO> reportedBugs;
    List<BugDTO> assignedBugs;

    public UserDetailsDTO(UserDTO userDto, List<BugDTO> reportedBugs, List<BugDTO> assignedBugs) {
        this.userDTO = userDto;
        this.reportedBugs = reportedBugs;
        this.assignedBugs = assignedBugs;
    }

    public Long getId() {
        return userDTO.getId();
    }

    public String getLogin() {
        return userDTO.getLogin();
    }

    public String getFirstName() {
        return userDTO.getFirstName();
    }

    public String getLastName() {
        return userDTO.getLastName();
    }

    public String getEmail() {
        return userDTO.getEmail();
    }

    public String getUserRole() {
        return userDTO.getUserRole();
    }

    public List<BugDTO> getReportedBugs() {
        return reportedBugs;
    }

    public List<BugDTO> getAssignedBugs() {
        return assignedBugs;
    }


}
