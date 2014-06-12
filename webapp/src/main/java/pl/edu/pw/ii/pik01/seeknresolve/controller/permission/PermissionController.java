package pl.edu.pw.ii.pik01.seeknresolve.controller.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.PermissionDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionChecker;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionService;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionChecker permissionChecker;

    @Autowired
    public PermissionController(PermissionService permissionService, PermissionChecker permissionChecker) {
        this.permissionService = permissionService;
        this.permissionChecker = permissionChecker;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<PermissionDTO>> getAll() {
        return new Response<>(permissionService.getAll(), Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/notInRole/{roleName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<PermissionDTO>> getNotInRole(@PathVariable("roleName") String roleName) {
        return new Response<>(permissionService.getNotInRole(roleName), Response.Status.RECEIVED);
    }

    @PreAuthorize("hasPermission(null, 'permission:create')")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PermissionDTO> create(@RequestBody PermissionDTO permissionDTO) {
        try {
            PermissionDTO created = permissionService.createAndSaveNewPermission(permissionDTO);
            return new Response<>(created, Response.Status.CREATED);
        } catch (PersistenceException e) {
            return new Response<>(null, Response.Status.NOT_CREATED);
        }
    }

    @RequestMapping(value = "/{permissionName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PermissionDTO> get(@PathVariable("permissionName") String permissionName) {
        return new Response<>(permissionService.get(permissionName), Response.Status.RECEIVED);
    }

    @PreAuthorize("hasPermission(null, 'permission:delete')")
    @RequestMapping(value = "/{permissionName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> delete(@PathVariable("permissionName") String permissionName) {
        permissionService.delete(permissionName);
        return new Response<>(permissionName, Response.Status.DELETED);
    }

    @RequestMapping(value = "/hasPermission", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> hasPermission(@RequestBody PermissionDTO permissionDTO) {
        Boolean hasPermission = permissionChecker.hasPermission(permissionService.createPermissionFromDTO(permissionDTO));
        return new Response<>(hasPermission.toString(), Response.Status.RECEIVED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
