package org.seeknresolve.controller.project;

import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.service.project.ProjectRevisionService;
import org.seeknresolve.service.response.Response;
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
@RequestMapping("/projectRevision")
public class ProjectRevisionController {
    private static final Logger log = LoggerFactory.getLogger(ProjectRevisionController.class);

    private ProjectRevisionService projectRevisionService;

    @Autowired
    public ProjectRevisionController(ProjectRevisionService projectRevisionService) {
        this.projectRevisionService = projectRevisionService;
    }

    @PreAuthorize("hasPermission(#id, 'Project', 'project:view') || hasPermission(#id, 'Project', 'project:everything')")
    @RequestMapping(value = "/all/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RevisionDiffDTO>> getAll(@PathVariable Long id) {
        try {
            return new Response<>(projectRevisionService.getAllForProject(id), Response.Status.RECEIVED);
        } catch (Exception e) {
            log.error("{}", e);
            throw new EntityNotFoundException("");
        }
    }
}
