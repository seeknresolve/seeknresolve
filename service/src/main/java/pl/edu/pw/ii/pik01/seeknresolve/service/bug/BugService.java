package pl.edu.pw.ii.pik01.seeknresolve.service.bug;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.BugRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BugService {
    private BugRepository bugRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    @Autowired
    public BugService(BugRepository bugRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.bugRepository = bugRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    //TODO: obsluzyc usera dobrze
    public List<BugDTO> getAllBugsForCurrentUser(User loggedUser) {
        return Lists.newArrayList(bugRepository.findAll()).stream().
                map(bug -> createDTOFromBug(bug)).collect(Collectors.toList());
    }

    public BugDTO createAndSaveNewBug(BugDTO bugDTO) {
        Bug bug = bugRepository.save(createBugFromDTO(bugDTO));
        if(bug == null) {
            throw new PersistenceException("Cannot save bug with tag: " + bugDTO.getTag());
        }
        return createDTOFromBug(bug);
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
            throw buildNotFoundException(tag);
        }
        return createDTOFromBug(bug);
    }

    private BugDTO createDTOFromBug(Bug bug) {
        BugDTO bugDTO = new BugDTO();
        bugDTO.setDateCreated(bug.getDateCreated());
        bugDTO.setDateModified(bug.getDateModified());
        bugDTO.setTag(bug.getTag());
        bugDTO.setName(bug.getName());
        bugDTO.setDescription(bug.getDescription());
        bugDTO.setState(bug.getState());
        bugDTO.setPriority(bug.getPriority());
        bugDTO.setProjectId(bug.getProject().getId());
        bugDTO.setReporterId(bug.getReporter().getId());
        bugDTO.setAssigneeId(bug.getAssignee().getId());
        return bugDTO;
    }

    public void deleteBugWithTag(String tag) {
        if(bugRepository.exists(tag)) {
            bugRepository.delete(tag);
        } else {
            throw buildNotFoundException(tag);
        }
    }

    private EntityNotFoundException buildNotFoundException(String tag) {
        return new EntityNotFoundException("Bug with tag: " + tag + " not found.");
    }
}
