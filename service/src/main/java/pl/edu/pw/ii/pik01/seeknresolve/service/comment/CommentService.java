package pl.edu.pw.ii.pik01.seeknresolve.service.comment;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.CommentDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Comment;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.BugRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.CommentRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    //TODO: UserRepository i UserService? Trochę śmierdzi
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private BugRepository bugRepository;

    private UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, BugRepository bugRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.bugRepository = bugRepository;
        this.userService = userService;
    }

    public List<CommentDTO> getAllByAuthorId(Long userId) {
        return getAllByAuthor(userRepository.findOne(userId));
    }

    public List<CommentDTO> getAllOfLoggedUser() {
        return getAllByAuthor(userService.getLoggedUser());
    }

    private List<CommentDTO> getAllByAuthor(User author) {
        List<Comment> commentList = commentRepository.findByAuthor(author);
        return createCommentDTOList(commentList);
    }

    public List<CommentDTO> getAllByBugTag(String bugTag) {
        List<Comment> commentList = commentRepository.findByBug(bugRepository.findOne(bugTag));
        return createCommentDTOList(commentList);
    }

    public CommentDTO getById(Long id) {
        Comment comment = commentRepository.findOne(id);
        User author = userRepository.findOne(comment.getId());
        return DtosFactory.createCommentDTO(comment, author.getLogin());
    }

    public CommentDTO createAndSaveNewComment(CommentDTO commentDTO, User author) {
        commentDTO.setDateCreated(DateTime.now());
        commentDTO.setAuthorId(author.getId());
        Comment comment = commentRepository.save(createCommentFromDTO(commentDTO));
        if (comment == null) {
            throw new PersistenceException("Cannot save comment made by:" + userService.getLoggedUser());
        }
        return DtosFactory.createCommentDTO(comment, author.getLogin());
    }

    private Comment createCommentFromDTO(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setDateCreated(commentDTO.getDateCreated());
        comment.setContent(commentDTO.getContent());
        comment.setAuthor(userRepository.findOneByLogin(commentDTO.getAuthorLogin()));
        comment.setBug(bugRepository.findOne(commentDTO.getBugTag()));
        return comment;
    }

    private List<CommentDTO> createCommentDTOList(List<Comment> commentList) {
        return commentList.
                stream().
                map(comment -> DtosFactory.createCommentDTO(comment, userRepository.findOne(comment.getAuthor().getId()).getLogin())).
                collect(Collectors.toList());
    }
}
