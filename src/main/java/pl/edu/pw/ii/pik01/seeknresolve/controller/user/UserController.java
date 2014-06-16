package pl.edu.pw.ii.pik01.seeknresolve.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ChangePasswordDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.CreateUserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDetailsDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'user:create')")
    public Response<UserDTO> create(@RequestBody CreateUserDTO userDTO) {
        UserDTO user = userService.createAndSaveNewUser(userDTO);
        return new Response<>(user, Response.Status.CREATED);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasPermission(null, 'user:change_password')")
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

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePersistenceErrors(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleSecurityException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        log.error("BAD_REQUEST", e);
    }
}
