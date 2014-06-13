package pl.edu.pw.ii.pik01.seeknresolve.service.bug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDetailsDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.CommentDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Comment;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.*;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BugService {
    private BugRepository bugRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private UserProjectRoleRepository userProjectRoleRepository;

    @Autowired
    public BugService(BugRepository bugRepository, ProjectRepository projectRepository,
                      UserRepository userRepository, CommentRepository commentRepository,
                      UserProjectRoleRepository userProjectRoleRepository) {
        this.bugRepository = bugRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
    }

    @Transactional(readOnly = true)
    public List<BugDTO> getAllPermittedBugs(User user) {
        List<UserProjectRole> projectRoles = userProjectRoleRepository.findByUser(user);
        return projectRoles.stream()
                .map(projectRole -> projectRole.getProject().getBugs())
                .flatMap(bugList -> bugList.stream())
                .map(bug -> DtosFactory.createBugDTO(bug))
                .collect(Collectors.toList());
    }

    @Transactional
    public BugDTO createAndSaveNewBug(BugDTO bugDTO) {
        Bug savedBug = bugRepository.save(createBugFromDTO(bugDTO));
        if(savedBug == null) {
            throw new PersistenceException("Cannot save bug with tag: " + bugDTO.getTag());
        }
        return DtosFactory.createBugDTO(savedBug);
    }

    private Bug createBugFromDTO(BugDTO bugDTO) {
        Bug bug = new Bug();
        bug.setTag(bugDTO.getTag());
        bug.setProject(projectRepository.findOne(bugDTO.getProjectId()));
        bug.setReporter(userRepository.findOne(bugDTO.getReporterId()));
        updateBugFromDTO(bug, bugDTO);
        return bug;
    }

    @Transactional(readOnly = true)
    public BugDetailsDTO getBugWithTag(String tag) {
        Bug bug = bugRepository.findOne(tag);
        if(bug == null) {
            throw bugNotFoundException(tag);
        }

        BugDTO bugDTO = DtosFactory.createBugDTO(bug);
        List<CommentDTO> comments = createCommentDTOList(commentRepository.findByBug(bug));
        return DtosFactory.createBugDetailsDTO(bugDTO, comments);
    }

    @Transactional
    public void deleteBugWithTag(String tag) {
        if(bugRepository.exists(tag)) {
            bugRepository.delete(tag);
        } else {
            throw bugNotFoundException(tag);
        }
    }

    private EntityNotFoundException bugNotFoundException(String tag) {
        return new EntityNotFoundException("Bug with tag: " + tag + " not found.");
    }

    private List<CommentDTO> createCommentDTOList(List<Comment> commentList) {
        return commentList.
                stream().
                map(comment -> DtosFactory.createCommentDTO(comment, userRepository.findOne(comment.getAuthor().getId()).getLogin())).
                collect(Collectors.toList());
    }

    @Transactional
    public BugDTO updateBug(BugDTO bugDTO) {
        Bug bug = bugRepository.findOne(bugDTO.getTag());
        updateBugFromDTO(bug, bugDTO);
        Bug updatedBug = bugRepository.save(bug);
        if(updatedBug == null) {
            throw new PersistenceException("Cannot update bug " + bugDTO.getTag());
        }
        return DtosFactory.createBugDTO(updatedBug);
    }

    private void updateBugFromDTO(Bug bug, BugDTO bugDTO) {
        bug.setName(bugDTO.getName());
        bug.setDescription(bugDTO.getDescription());
        bug.setState(bugDTO.getState() != null ? bugDTO.getState() : Bug.State.NEW);
        bug.setPriority(bugDTO.getPriority());
        if(bugDTO.getAssigneeId() != null) {
            bug.setAssignee(userRepository.findOne(bugDTO.getAssigneeId()));
        }
    }

    @Transactional(readOnly = true)
    public List<BugDTO> search(String query) {
        List<Bug> foundBugs = bugRepository.queryOnFields(query, "tag", "name", "description");
        return foundBugs.stream()
                .map(bug -> DtosFactory.createBugDTO(bug))
                .collect(Collectors.toList());
    }
}
