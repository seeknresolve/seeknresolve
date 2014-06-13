package pl.edu.pw.ii.pik01.seeknresolve.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RevisionDiffDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.project.ProjectRevisionService;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;

import java.beans.IntrospectionException;
import java.util.List;

@RestController
@RequestMapping("/projectRevision")
public class ProjectRevisionController {

    private ProjectRevisionService projectRevisionService;

    @Autowired
    public ProjectRevisionController(ProjectRevisionService projectRevisionService) {
        this.projectRevisionService = projectRevisionService;
    }

    @PreAuthorize("hasPermission(#id, 'Project', 'project:view') || hasPermission(#id, 'Project', 'project:everything')")
    @RequestMapping(value = "/all/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RevisionDiffDTO>> getAll(@PathVariable Long id) {
        try {
            return new Response<>(projectRevisionService.getAllForProject(id), Response.Status.RECEIVED);
        } catch (Exception e) {
            e.printStackTrace();
            //FIXME exception:
            throw new EntityNotFoundException("");
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
