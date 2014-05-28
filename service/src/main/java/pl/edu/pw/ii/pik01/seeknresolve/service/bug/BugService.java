package pl.edu.pw.ii.pik01.seeknresolve.service.bug;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.BugRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BugService {
    private BugRepository bugRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private UserProjectRoleRepository userProjectRoleRepository;
    private DtosFactory dtosFactory;

    @Autowired
    public BugService(BugRepository bugRepository, ProjectRepository projectRepository,
                      UserRepository userRepository, UserProjectRoleRepository userProjectRoleRepository, DtosFactory dtosFactory) {
        this.bugRepository = bugRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.dtosFactory = dtosFactory;
    }

    public List<BugDTO> getAll(User user) {
        List<UserProjectRole> projectRoles = userProjectRoleRepository.findByUser(user);
        return projectRoles.stream().parallel()
                .map(projectRole -> projectRole.getProject().getBugs())
                .flatMap(bugList -> bugList.stream())
                .map(bug -> dtosFactory.createBugDTO(bug))
                .collect(Collectors.toList());
    }

    public BugDTO createAndSaveNewBug(BugDTO bugDTO) {
        Bug bug = bugRepository.save(createBugFromDTO(bugDTO));
        if(bug == null) {
            throw new PersistenceException("Cannot save bug with tag: " + bugDTO.getTag());
        }
        return dtosFactory.createBugDTO(bug);
    }

    private Bug createBugFromDTO(BugDTO bugDTO) {
        Bug bug = new Bug();
        bug.setDateCreated(DateTime.now());
        bug.setDateModified(DateTime.now());
        bug.setTag(bugDTO.getTag());
        bug.setName(bugDTO.getName());
        bug.setDescription(bugDTO.getDescription());
        bug.setState(bugDTO.getState());
        bug.setPriority(bugDTO.getPriority());
        bug.setProject(projectRepository.findOne(bugDTO.getProjectId()));
        bug.setReporter(userRepository.findOne(bugDTO.getReporterId()));
        bug.setAssignee(userRepository.findOne(bugDTO.getAssigneeId()));
        return bug;
    }

    public BugDTO getBugWithTag(String tag) {
        Bug bug = bugRepository.findOne(tag);
        if(bug == null) {
            throw bugNotFoundException(tag);
        }
        return dtosFactory.createBugDTO(bug);
    }

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
}
