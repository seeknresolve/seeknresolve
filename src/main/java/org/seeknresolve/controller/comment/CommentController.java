package org.seeknresolve.controller.comment;

import org.seeknresolve.domain.dto.CommentDTO;
import org.seeknresolve.service.comment.CommentService;
import org.seeknresolve.service.response.Response;
import org.seeknresolve.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<CommentDTO> create(@RequestBody CommentDTO commentDTO) {
        try {
            CommentDTO created = commentService.createAndSaveNewComment(commentDTO, userService.getLoggedUser());
            return new Response<>(created, Response.Status.CREATED);
        } catch (PersistenceException e) {
            return new Response<>(null, Response.Status.NOT_CREATED);
        }
    }
    @RequestMapping(value = "/bug/{bugTag}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<CommentDTO>> getByBugTag(@PathVariable("bugTag") String bugTag) {
        List<CommentDTO> commentList = commentService.getAllByBugTag(bugTag);
        return new Response<>(commentList, Response.Status.RECEIVED);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<CommentDTO>> getByUserId(@PathVariable("userId") Long userId) {
        List<CommentDTO> commentList = commentService.getAllByAuthorId(userId);
        return new Response<>(commentList, Response.Status.RECEIVED);
    }
}
