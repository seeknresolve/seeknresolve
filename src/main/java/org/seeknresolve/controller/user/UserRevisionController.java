package org.seeknresolve.controller.user;

import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.service.response.ErrorResponse;
import org.seeknresolve.service.response.Response;
import org.seeknresolve.service.user.UserRevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/userRevision")
public class UserRevisionController {

    private final UserRevisionService userRevisionService;

    @Autowired
    public UserRevisionController(UserRevisionService userRevisionService) {
        this.userRevisionService = userRevisionService;
    }

    @PreAuthorize("hasPermission(#null, 'user:view_history')")
    @RequestMapping(value = "/all/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RevisionDiffDTO>> getAll(@PathVariable String login) {
        try {
            return new Response<>(userRevisionService.getAllForUser(login), Response.Status.RECEIVED);
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