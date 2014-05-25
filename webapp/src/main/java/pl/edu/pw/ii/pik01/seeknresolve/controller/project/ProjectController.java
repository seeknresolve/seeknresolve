package pl.edu.pw.ii.pik01.seeknresolve.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.project.ProjectService;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;
    private UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProjectDTO> create(@RequestBody ProjectDTO projectDTO) {
        try {
            ProjectDTO created = projectService.createAndSaveNewProject(projectDTO);
            return new Response<>(created, Response.Status.CREATED);
        } catch (PersistenceException e) {
            return new Response<>(null, Response.Status.NOT_CREATED);
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ProjectDTO>> getAll() {
        return new Response<>(projectService.getAll(userService.getLoggedUser()), Response.Status.RECEIVED);
    }

    @PreAuthorize("hasPermission(#id, 'Project', 'project:view') || hasPermission(#id, 'Project', 'project:everything')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProjectDTO> get(@PathVariable Long id) {
        try {
            ProjectDTO found = projectService.getById(id);
            return new Response<>(found, Response.Status.RECEIVED);
        } catch (EntityNotFoundException e) {
            return new Response<>(null, Response.Status.NOT_RECEIVED);
        }
    }

    @PreAuthorize("hasPermission(#id, 'Project', 'project:update') || hasPermission(#id, 'Project', 'project:everything')")
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProjectDTO> update(@RequestBody ProjectDTO projectDTO) {
        try {
            ProjectDTO updated = projectService.update(projectDTO);
            return new Response<>(updated, Response.Status.UPDATED);
        } catch (EntityNotFoundException e) {
            return new Response<>(null, Response.Status.NOT_UPDATED);
        }
    }

    @PreAuthorize("hasPermission(#id, 'Project', 'project:delete') || hasPermission(#id, 'Project', 'project:everything')")
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