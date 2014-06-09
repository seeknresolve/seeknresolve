package pl.edu.pw.ii.pik01.seeknresolve.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDetailsDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserProjectRoleStoreDTO;
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
            ProjectDTO created = projectService.createAndSaveNewProject(projectDTO, userService.getLoggedUser());
            return new Response<>(created, Response.Status.CREATED);
        } catch (PersistenceException e) {
            return new Response<>(null, Response.Status.NOT_CREATED);
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ProjectDTO>> getAll() {
        return new Response<>(projectService.getAllPermittedProjects(userService.getLoggedUser()), Response.Status.RECEIVED);
    }

    @PreAuthorize("hasPermission(#id, 'Project', 'project:view') || hasPermission(#id, 'Project', 'project:everything')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ProjectDetailsDTO> get(@PathVariable Long id) {
        try {
            ProjectDetailsDTO found = projectService.getById(id);
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
    public Response<Long> delete(@PathVariable Long id) {
        try {
            projectService.delete(id);
            return new Response<>(id, Response.Status.DELETED);
        } catch (EntityNotFoundException e) {
            return new Response<>(null, Response.Status.NOT_DELETED);
        }
    }

    @PreAuthorize("hasPermission(#id, 'Project', 'project:view') || hasPermission(#id, 'Project', 'project:everything')")
    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<UserDTO>> getUsersWithRolesOnProject(@PathVariable("id") Long id) {
        return new Response<>(userService.getAllUserWithRolesOnProject(id), Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/grantRole", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> grantRoleForUserToProject(@RequestBody UserProjectRoleStoreDTO userProjectRoleStoreDTO) {
        return new Response<>(projectService.grantRoleForUserToProject(
                    userProjectRoleStoreDTO.getRole(),
                    userProjectRoleStoreDTO.getUserId(),
                    userProjectRoleStoreDTO.getProjectId()
                ) != null ? "success" : "failure",
                Response.Status.CREATED);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ProjectDTO>> search(@RequestParam("query") String query) {
        return new Response<>(projectService.search(query), Response.Status.RECEIVED);
    }

}