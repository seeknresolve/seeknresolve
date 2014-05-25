package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private UserProjectRoleRepository userProjectRoleRepository;
    private DtosFactory dtosFactory;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserProjectRoleRepository userProjectRoleRepository, DtosFactory dtosFactory) {
        this.projectRepository = projectRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.dtosFactory = dtosFactory;
    }

    public ProjectDTO createAndSaveNewProject(ProjectDTO projectDTO) {
        projectDTO.setDateCreated(DateTime.now());
        Project project = createProjectFromDTO(projectDTO);
        Project savedProject = projectRepository.save(project);
        if (savedProject == null) {
            throw new PersistenceException("Cannot save project with id: " + projectDTO.getId());
        }
        return dtosFactory.createProjectDTO(savedProject);
    }

    public ProjectDTO getById(Long id) {
        Project project = projectRepository.findOne(id);
        if (project == null) {
            throw getEntityNotFoundException(id);
        }
        return dtosFactory.createProjectDTO(project);
    }

    public List<ProjectDTO> getAll(User user) {
        List<UserProjectRole> rolesOnProjects = userProjectRoleRepository.findByUser(user);
        return rolesOnProjects.stream()
                .map(userProjectRole -> userProjectRole.getProject())
                .map(project -> dtosFactory.createProjectDTO(project))
                .collect(Collectors.toList());
    }

    public ProjectDTO update(ProjectDTO projectDTO) {
        Project project = projectRepository.findOne(projectDTO.getId());
        if (project == null) {
            throw getEntityNotFoundException(projectDTO.getId());
        }
        //TODO: nie aktualizuje
        project.update(createProjectFromDTO(projectDTO));
        return dtosFactory.createProjectDTO(project);
    }

    public void delete(Long id) {
        if(!projectRepository.exists(id)) {
            throw getEntityNotFoundException(id);
        }
        projectRepository.delete(id);
    }

    private EntityNotFoundException getEntityNotFoundException(Long id) {
        return new EntityNotFoundException("Project with id: " + id + " not found.");
    }

    private Project createProjectFromDTO(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setDateCreated(projectDTO.getDateCreated());
        return project;
    }
}
