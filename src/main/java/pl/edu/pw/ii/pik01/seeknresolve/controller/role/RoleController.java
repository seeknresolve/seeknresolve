package pl.edu.pw.ii.pik01.seeknresolve.controller.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ChangeRolePermissionDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RoleDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;
import pl.edu.pw.ii.pik01.seeknresolve.service.role.RoleService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public  RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RoleDTO>> getAllUserRoles() {
        List<RoleDTO> userRoles = roleService.getAllUserRoles();
        return new Response<>(userRoles, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RoleDTO>> getAllProjectRoles() {
        List<RoleDTO> userRoles = roleService.getAllProjectRoles();
        return new Response<>(userRoles, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/details/{roleName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<RoleDTO> get(@PathVariable("roleName") String roleName) {
        return new Response<>(roleService.getRole(roleName), Response.Status.RECEIVED);
    }

    @PreAuthorize("hasPermission(null, 'role:add_permission')")
    @RequestMapping(value = "/addPermission", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ChangeRolePermissionDTO> addPermission(@RequestBody ChangeRolePermissionDTO changeRolePermissionDTO) {
        roleService.addPermissionToRole(changeRolePermissionDTO.getRoleName(), changeRolePermissionDTO.getPermissionName());
        return new Response<>(changeRolePermissionDTO, Response.Status.CREATED);
    }

    @PreAuthorize("hasPermission(null, 'role:delete_permission')")
    @RequestMapping(value = "/deletePermission", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<ChangeRolePermissionDTO> deletePermission(@RequestBody ChangeRolePermissionDTO changeRolePermissionDTO) {
        roleService.deletePermissionFromRole(changeRolePermissionDTO.getRoleName(), changeRolePermissionDTO.getPermissionName());
        return new Response<>(changeRolePermissionDTO, Response.Status.DELETED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

}
