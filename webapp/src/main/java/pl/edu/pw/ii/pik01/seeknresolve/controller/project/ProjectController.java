package pl.edu.pw.ii.pik01.seeknresolve.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.project.ProjectService;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;

import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProjectDTO> create(@RequestBody ProjectDTO projectDTO) {
        try {
            ProjectDTO created = projectService.create(projectDTO);
            return new Response<>(created, Response.Status.CREATED);
        } catch (PersistenceException e) {
            return new Response<>(null, Response.Status.NOT_CREATED);
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ProjectDTO>> get() {
        return new Response<>(projectService.getAll(), Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProjectDTO> get(@PathVariable Long id) {
        try {
            ProjectDTO found = projectService.get(id);
            return new Response<>(found, Response.Status.RECEIVED);
        } catch (EntityNotFoundException e) {
            return new Response<>(null, Response.Status.NOT_RECEIVED);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProjectDTO> update(@RequestBody ProjectDTO projectDTO) {
        try {
            ProjectDTO updated = projectService.update(projectDTO);
            return new Response<>(updated, Response.Status.UPDATED);
        } catch (EntityNotFoundException e) {
            return new Response<>(null, Response.Status.NOT_UPDATED);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Long> delete(@PathVariable("id") Long id) {
        try {
            projectService.delete(id);
            return new Response<>(id, Response.Status.DELETED);
        } catch (EntityNotFoundException e) {
            return new Response<>(null, Response.Status.NOT_DELETED);
        }
    }
}