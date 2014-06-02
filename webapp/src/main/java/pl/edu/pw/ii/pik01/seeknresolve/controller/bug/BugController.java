package pl.edu.pw.ii.pik01.seeknresolve.controller.bug;

import com.google.gson.Gson;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDetailsDTO;
import pl.edu.pw.ii.pik01.seeknresolve.service.bug.BugService;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.Response;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/bug")
public class BugController {
    private BugService bugService;
    private UserService userService;

    @Autowired
    public BugController(BugService bugService, UserService userService) {
        this.bugService = bugService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BugDTO> create(@RequestBody BugDTO bugDTO) {
        BugDTO createdBug = bugService.createAndSaveNewBug(bugDTO);
        return new Response<>(createdBug, Response.Status.CREATED);
    }

    @RequestMapping(value = "/details/{tag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<BugDetailsDTO> get(@PathVariable("tag") String tag) {
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

        String json = new Gson().toJson(bugs);
        JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(json.getBytes()));
        JasperPrint jasperPrint = JasperFillManager.fillReport("src/main/resources/reports/Bugs.jasper", new HashMap<>(), jsonDataSource);

        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "attachment; filename=" + "bugsReport.pdf");
        response.setContentLength(pdfBytes.length);

        response.getOutputStream().write(pdfBytes);
        response.flushBuffer();
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

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleSecurityException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
