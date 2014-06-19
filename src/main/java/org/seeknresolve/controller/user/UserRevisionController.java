package org.seeknresolve.controller.user;

import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.service.response.Response;
import org.seeknresolve.service.user.UserRevisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/userRevision")
public class UserRevisionController {
    private static final Logger log = LoggerFactory.getLogger(UserRevisionController.class);

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
            log.error("{}", e);
            throw new EntityNotFoundException("");
        }
    }
}
