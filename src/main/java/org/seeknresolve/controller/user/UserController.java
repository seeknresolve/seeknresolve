package org.seeknresolve.controller.user;

import org.seeknresolve.domain.dto.ChangePasswordDTO;
import org.seeknresolve.domain.dto.CreateUserDTO;
import org.seeknresolve.domain.dto.UserDTO;
import org.seeknresolve.domain.dto.UserDetailsDTO;
import org.seeknresolve.service.response.Response;
import org.seeknresolve.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasPermission(null, 'user:create')")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserDTO> create(@RequestBody CreateUserDTO userDTO) {
        UserDTO user = userService.createAndSaveNewUser(userDTO);
        return new Response<>(user, Response.Status.CREATED);
    }

    @PreAuthorize("hasPermission(null, 'user:change_password')")
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
        return new Response<>("changed", Response.Status.UPDATED);
    }

    @RequestMapping(value = "/details/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserDetailsDTO> get(@PathVariable("login") String login) {
        UserDetailsDTO user = userService.findByLogin(login);
        return new Response<>(user, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.getAllUsers();
        return new Response<>(users, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/logged", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserDTO> getLoggedUser() {
        return new Response<>(userService.getLoggedUserDTO(), Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/notAssignedToProject/{projectId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<UserDTO>> findNotAssignedToProject(@PathVariable("projectId") Long projectId) {
        return new Response<>(userService.findNotAssignedToProject(projectId), Response.Status.RECEIVED);
    }

    @PreAuthorize("hasPermission(null, 'user:update')")
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserDTO> update(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userDTO);
        return new Response<>(updatedUser, Response.Status.UPDATED);
    }
}
