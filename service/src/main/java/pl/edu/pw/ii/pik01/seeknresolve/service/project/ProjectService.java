package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.ProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.RolesConstants;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private UserProjectRoleRepository userProjectRoleRepository;
    private RoleRepository roleRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserProjectRoleRepository userProjectRoleRepository, RoleRepository roleRepository) {
        this.projectRepository = projectRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.roleRepository = roleRepository;
    }

    public ProjectDTO createAndSaveNewProject(ProjectDTO projectDTO, User user) {
        projectDTO.setDateCreated(DateTime.now());
        Project project = createProjectFromDTO(projectDTO);
        Project savedProject = projectRepository.save(project);
        if (savedProject == null) {
            throw new PersistenceException("Cannot save project with id: " + projectDTO.getId());
        }
        grantAndSaveRoleForProject(user, savedProject);
        return DtosFactory.createProjectDTO(savedProject);
    }

    private void grantAndSaveRoleForProject(User user, Project project) {
        ProjectRole projectRole = getProjectRole(RolesConstants.PROJECT_MANAGER_ROLE_NAME);
        UserProjectRole userProjectRole = new UserProjectRole(user, project, projectRole);
        userProjectRoleRepository.save(userProjectRole);
    }

    private ProjectRole getProjectRole(String roleName) {
        return (ProjectRole)roleRepository.findOne(roleName);
    }

    public ProjectDTO getById(Long id) {
        Project project = projectRepository.findOne(id);
        if (project == null) {
            throw getEntityNotFoundException(id);
        }
        return DtosFactory.createProjectDTO(project);
    }

    public List<ProjectDTO> getAll(User user) {
        List<UserProjectRole> rolesOnProjects = userProjectRoleRepository.findByUser(user);
        return rolesOnProjects.stream()
                .map(userProjectRole -> userProjectRole.getProject())
                .map(project -> DtosFactory.createProjectDTO(project))
                .collect(Collectors.toList());
    }

    public ProjectDTO update(ProjectDTO projectDTO) {
        Project project = projectRepository.findOne(projectDTO.getId());
        if (project == null) {
            throw getEntityNotFoundException(projectDTO.getId());
        }
        //TODO: nie aktualizuje
        project.update(createProjectFromDTO(projectDTO));
        return DtosFactory.createProjectDTO(project);
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
