package org.seeknresolve.controller.bug;

import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.service.bug.BugRevisionService;
import org.seeknresolve.service.response.ErrorResponse;
import org.seeknresolve.service.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bugRevision")
public class BugRevisionController {

    private BugRevisionService bugRevisionService;

    @Autowired
    public BugRevisionController(BugRevisionService bugRevisionService) {
        this.bugRevisionService = bugRevisionService;
    }

    @RequestMapping(value = "/all/{bugTag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RevisionDiffDTO>> getAll(@PathVariable String bugTag) {
        try {
            return new Response<>(bugRevisionService.getAllForBug(bugTag), Response.Status.RECEIVED);
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
