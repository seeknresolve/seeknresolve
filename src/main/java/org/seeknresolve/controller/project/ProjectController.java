package org.seeknresolve.controller.project;

import net.sf.jasperreports.engine.JRException;
import org.seeknresolve.domain.dto.ProjectDTO;
import org.seeknresolve.domain.dto.ProjectDetailsDTO;
import org.seeknresolve.domain.dto.UserDTO;
import org.seeknresolve.infrastructure.reports.Printer;
import org.seeknresolve.service.project.ProjectService;
import org.seeknresolve.service.response.Response;
import org.seeknresolve.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private ProjectService projectService;
    private UserService userService;
    private DataSource dataSource;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, DataSource dataSource) {
        this.projectService = projectService;
        this.userService = userService;
        this.dataSource = dataSource;
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

    @RequestMapping(value = "{projectId}/grantRole/{role}/user/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> grantRoleForUserToProject(@PathVariable("projectId") Long projectId, @PathVariable("role") String role, @PathVariable("userId") Long userId) {
        return new Response<>(projectService.grantRoleForUserToProject(role, userId, projectId) != null ? "success" : "failure",
                Response.Status.CREATED);
    }

    @RequestMapping(value = "{projectId}/revokeRole/user/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Long> revokeRoleFromUserToProject(@PathVariable("projectId") Long projectId, @PathVariable("userId") Long userId) {
        return new Response<>(projectService.revokeRoleFromUserToProject(userId, projectId), Response.Status.DELETED);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<ProjectDTO>> search(@RequestParam("query") String query) {
        return new Response<>(projectService.search(query, userService.getLoggedUser()), Response.Status.RECEIVED);
    }

    @ResponseBody
    @RequestMapping(value = "/printUsers", method=RequestMethod.GET)
    public void printAll(HttpServletResponse response) throws IOException, JRException {
        Printer.getBuilder()
                .setConnection(DataSourceUtils.getConnection(dataSource))
                .setJasperFileName("userProjects")
                .addParameter("loggedUser", userService.getLoggedUser().getLogin())
                .addParameter("userId", userService.getLoggedUser().getId())
                .setOutputResponse(response)
                .build()
                .print();
    }

}