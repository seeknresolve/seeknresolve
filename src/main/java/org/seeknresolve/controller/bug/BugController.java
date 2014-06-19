package org.seeknresolve.controller.bug;

import net.sf.jasperreports.engine.JRException;
import org.seeknresolve.domain.dto.BugDTO;
import org.seeknresolve.domain.dto.BugDetailsDTO;
import org.seeknresolve.reports.Printer;
import org.seeknresolve.service.bug.BugService;
import org.seeknresolve.service.response.Response;
import org.seeknresolve.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bug")
public class BugController {
    private Logger log = LoggerFactory.getLogger(BugController.class);

    private BugService bugService;
    private UserService userService;

    @Autowired
    public BugController(BugService bugService, UserService userService) {
        this.bugService = bugService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BugDTO> create(@RequestBody @Valid BugDTO bugDTO) {
        log.info("Bug creation started with dto: " + bugDTO);
        BugDTO createdBug = bugService.createAndSaveNewBug(bugDTO);
        return new Response<>(createdBug, Response.Status.CREATED);
    }

    @RequestMapping(value = "/details/{tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BugDetailsDTO> get(@PathVariable String tag) {
        BugDetailsDTO bug = bugService.getBugWithTag(tag);
        return new Response<>(bug, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<BugDTO>> getAll() {
        List<BugDTO> bugs = bugService.getAllPermittedBugs(userService.getLoggedUser());
        return new Response<>(bugs, Response.Status.RECEIVED);
    }

    @ResponseBody
    @RequestMapping(value = "/printAll", method=RequestMethod.GET)
    public void printAll(HttpServletResponse response) throws IOException, JRException {
        List<BugDTO> bugs = bugService.getAllPermittedBugs(userService.getLoggedUser());

        Printer.getBuilder()
                .setDataSource(bugs)
                .setJasperFileName("Bugs")
                .addParameter("loggedUser", userService.getLoggedUser().getLogin())
                .setOutputResponse(response)
                    .build()
                        .print();
    }

    @RequestMapping(value = "/{tag}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<String> delete(@PathVariable String tag) {
        bugService.deleteBugWithTag(tag);
        return new Response<>(tag, Response.Status.DELETED);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BugDTO> update(@RequestBody @Valid BugDTO bugDTO) {
        log.info("Bug update started with dto: " + bugDTO);
        BugDTO updatedBug = bugService.updateBug(bugDTO);
        return new Response<>(updatedBug, Response.Status.UPDATED);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<BugDTO>> search(@RequestParam("query") String query) {
        return new Response<>(bugService.search(query, userService.getLoggedUser()), Response.Status.RECEIVED);
    }
}
