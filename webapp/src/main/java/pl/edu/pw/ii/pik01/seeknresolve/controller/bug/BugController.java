package pl.edu.pw.ii.pik01.seeknresolve.controller.bug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.bug.BugService;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;

import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/bug")
public class BugController {
    private BugService bugService;

    @Autowired
    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BugDTO> create(@RequestBody BugDTO bugDTO) {
        BugDTO createdBug = bugService.createAndSaveNewBug(bugDTO);
        return new Response<>(createdBug, Response.Status.CREATED);
    }

    @RequestMapping(value = "/{tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BugDTO> get(@PathVariable("tag") String tag) {
        BugDTO bug = bugService.getBugWithTag(tag);
        return new Response<>(bug, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<BugDTO>> getAll() {
        //TODO: get current user
        List<BugDTO> bugs = bugService.getAllBugsForCurrentUser(null);
        return new Response<>(bugs, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/{tag}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> delete(@PathVariable("tag") String tag) {
        bugService.deleteBugWithTag(tag);
        return new Response<>(tag, Response.Status.DELETED);
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
}