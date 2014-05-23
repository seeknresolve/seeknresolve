package pl.edu.pw.ii.pik01.seeknresolve.service.project;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private DtosFactory dtosFactory;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, DtosFactory dtosFactory) {
        this.projectRepository = projectRepository;
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

    public List<ProjectDTO> getAll() {
        List<ProjectDTO> result = new ArrayList<>();
        for (Project project: projectRepository.findAll()) {
            result.add(dtosFactory.createProjectDTO(project));
        }
        return result;
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
