package org.seeknresolve.service.bug;

import org.joda.time.DateTime;
import org.seeknresolve.domain.dto.BugDTO;
import org.seeknresolve.domain.dto.BugDetailsDTO;
import org.seeknresolve.domain.dto.CommentDTO;
import org.seeknresolve.domain.entity.*;
import org.seeknresolve.domain.repository.*;
import org.seeknresolve.service.bug.generator.BugTagGenerator;
import org.seeknresolve.service.common.DtosFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class BugService {
    private BugRepository bugRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private UserProjectRoleRepository userProjectRoleRepository;
    private BugTagGenerator bugTagGenerator;

    @Autowired
    public BugService(BugRepository bugRepository, ProjectRepository projectRepository,
                      UserRepository userRepository, CommentRepository commentRepository,
                      UserProjectRoleRepository userProjectRoleRepository, BugTagGenerator bugTagGenerator) {
        this.bugRepository = bugRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.bugTagGenerator = bugTagGenerator;
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
        Project project = projectRepository.findOne(bugDTO.getProjectId());
        bug.setTag(generateNextTagForBug(project));
        bug.setProject(project);
        bug.setReporter(userRepository.findOne(bugDTO.getReporterId()));
        updateBugDataFromDTO(bug, bugDTO);
        bug.setDateCreated(DateTime.now());
        return bug;
    }

    private String generateNextTagForBug(Project project) {
        String nextTag = bugTagGenerator.generateTag(project);
        incrementBugNumberAndSaveProject(project);
        return nextTag;
    }

    private void incrementBugNumberAndSaveProject(Project project) {
        project.incrementLastBugNumber();
        projectRepository.save(project);
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
        updateBugDataFromDTO(bug, bugDTO);
        Bug updatedBug = bugRepository.save(bug);
        if(updatedBug == null) {
            throw new PersistenceException("Cannot update bug " + bugDTO.getTag());
        }
        return DtosFactory.createBugDTO(updatedBug);
    }

    private void updateBugDataFromDTO(Bug bug, BugDTO bugDTO) {
        bug.setName(bugDTO.getName());
        bug.setDescription(setCorrectDescription(bugDTO));
        bug.setState(bugDTO.getState() != null ? bugDTO.getState() : Bug.State.NEW);
        bug.setPriority(bugDTO.getPriority());
        if(bugDTO.getAssigneeId() != null) {
            bug.setAssignee(userRepository.findOne(bugDTO.getAssigneeId()));
        }
    }

    private String setCorrectDescription(BugDTO bugDTO) {
        return bugDTO.getDescription() == null ? "" : bugDTO.getDescription();
    }

    @Transactional(readOnly = true)
    public List<BugDTO> search(String query, User user) {
        List<Bug> foundBugs = bugRepository.queryOnFields(query, "tag", "name", "description");
        return foundBugs.stream()
                .filter(userHasAccessRightsToSeeBug(user))
                .map(bug -> DtosFactory.createBugDTO(bug))
                .collect(Collectors.toList());
    }

    private Predicate<Bug> userHasAccessRightsToSeeBug(User user) {
        return bug -> {
            UserProjectRole userProjectRole = userProjectRoleRepository.findByUserAndProject(user, bug.getProject());
            return userProjectRole != null;
        };
    }
}
