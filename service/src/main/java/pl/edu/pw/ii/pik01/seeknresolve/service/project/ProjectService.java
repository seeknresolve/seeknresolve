package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDetailsDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserProjectRoleDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.ProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.*;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.RolesConstants;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private BugRepository bugRepository;
    private UserProjectRoleRepository userProjectRoleRepository;
    private RoleRepository roleRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, BugRepository bugRepository, UserProjectRoleRepository userProjectRoleRepository, RoleRepository roleRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.bugRepository = bugRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public ProjectDTO createAndSaveNewProject(ProjectDTO projectDTO, User user) {
        projectDTO.setDateCreated(DateTime.now());
        Project project = createProjectFromDTO(projectDTO);
        Project savedProject = projectRepository.save(project);
        if (savedProject == null) {
            throw new PersistenceException("Cannot save project with id: " + projectDTO.getId());
        }
        grantRoleForUserToProject(RolesConstants.PROJECT_MANAGER_ROLE_NAME, user, savedProject);
        return DtosFactory.createProjectDTO(savedProject);
    }

    private Long grantRoleForUserToProject(String role, User user, Project project) {
        ProjectRole projectRole = getProjectRole(role);
        UserProjectRole userProjectRole = new UserProjectRole(user, project, projectRole);
        return userProjectRoleRepository.save(userProjectRole).getId();
    }

    @Transactional
    public Long grantRoleForUserToProject(String role, Long userId, Long projectId) {
        User user = userRepository.findOne(userId);
        Project project = projectRepository.findOne(projectId);
        ProjectRole projectRole = getProjectRole(role);
        UserProjectRole userProjectRole = new UserProjectRole(user, project, projectRole);
        return userProjectRoleRepository.save(userProjectRole).getId();
    }

    private ProjectRole getProjectRole(String roleName) {
        return (ProjectRole)roleRepository.findOne(roleName);
    }

    @Transactional
    public ProjectDetailsDTO getById(Long projectId) {
        Project project = projectRepository.findOne(projectId);
        if (project == null) {
            throw getEntityNotFoundException(projectId);
        }
        List<UserProjectRoleDTO> users = getProjectUserDTOs(project);
        List<BugDTO> bugDTOs = getBugDTOs(project);
        ProjectDTO projectDTO = DtosFactory.createProjectDTO(project);
        return DtosFactory.createProjectDetailsDTO(users, bugDTOs, projectDTO);
    }

    private List<UserProjectRoleDTO> getProjectUserDTOs(Project project) {
        return userProjectRoleRepository.findByProjectId(project.getId())
                .stream()
                .parallel()
                .map(DtosFactory::createProjectUserDTO)
                .collect(Collectors.toList());
    }

    private List<BugDTO> getBugDTOs(Project project) {
        return bugRepository.findByProject(project)
                .stream()
                .parallel()
                .map(DtosFactory::createBugDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ProjectDTO> getAllPermittedProjects(User user) {
        List<UserProjectRole> rolesOnProjects = userProjectRoleRepository.findByUser(user);
        return rolesOnProjects.stream()
                .map(userProjectRole -> userProjectRole.getProject())
                .map(project -> DtosFactory.createProjectDTO(project))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectDTO update(ProjectDTO projectDTO) {
        Project project = projectRepository.findOne(projectDTO.getId());
        if (project == null) {
            throw getEntityNotFoundException(projectDTO.getId());
        }
        //TODO: nie aktualizuje
        project.update(createProjectFromDTO(projectDTO));
        return DtosFactory.createProjectDTO(project);
    }

    @Transactional
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

    @Transactional
    public List<ProjectDTO> search(String query) {
        List<Project> foundBugs = projectRepository.queryOnFields(query, "name", "description");
        return foundBugs.stream()
                .map(project -> DtosFactory.createProjectDTO(project))
                .collect(Collectors.toList());
    }
}
