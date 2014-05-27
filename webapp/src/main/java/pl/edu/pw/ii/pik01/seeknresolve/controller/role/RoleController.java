package pl.edu.pw.ii.pik01.seeknresolve.controller.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RoleDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;
import pl.edu.pw.ii.pik01.seeknresolve.service.role.RoleService;

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

    @RequestMapping(value = "/{roleName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<RoleDTO> get(@PathVariable("roleName") String roleName) {
        return new Response<>(roleService.getRole(roleName), Response.Status.RECEIVED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

}
