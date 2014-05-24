package pl.edu.pw.ii.pik01.seeknresolve.service.common;

import org.springframework.stereotype.Component;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;

@Component
public class DtosFactory {
    public ProjectDTO createProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setDateCreated(project.getDateCreated());
        project.getBugs().stream().forEach(bug -> projectDTO.addBug(createBugDTO(bug)));
        return projectDTO;
    }

    public BugDTO createBugDTO(Bug bug) {
        BugDTO bugDTO = new BugDTO();
        bugDTO.setDateCreated(bug.getDateCreated());
        bugDTO.setDateModified(bug.getDateModified());
        bugDTO.setTag(bug.getTag());
        bugDTO.setName(bug.getName());
        bugDTO.setDescription(bug.getDescription());
        bugDTO.setState(bug.getState());
        bugDTO.setPriority(bug.getPriority());
        bugDTO.setProjectId(bug.getProject().getId());
        bugDTO.setProjectName(bug.getProject().getName());
        bugDTO.setReporterId(bug.getReporter().getId());
        bugDTO.setReporterName(String.join(" ", bug.getReporter().getFirstName(), bug.getReporter().getLastName()));
        bugDTO.setAssigneeId(bug.getAssignee().getId());
        bugDTO.setAssigneeName(String.join(" ", bug.getAssignee().getFirstName(), bug.getAssignee().getLastName()));
        return bugDTO;
    }
}
