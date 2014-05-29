package pl.edu.pw.ii.pik01.seeknresolve.controller.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.PermissionDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ProjectDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionService;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;

import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<PermissionDTO>> getAll() {
        return new Response<>(permissionService.getAll(), Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PermissionDTO> create(@RequestBody PermissionDTO permissionDTO) {
        try {
            PermissionDTO created = permissionService.createAndSaveNewPermission(permissionDTO);
            return new Response<>(created, Response.Status.CREATED);
        } catch (PersistenceException e) {
            return new Response<>(null, Response.Status.NOT_CREATED);
        }
    }
}
