package pl.edu.pw.ii.pik01.seeknresolve.service.bug;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RevisionDiffDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.BugRepository;
import pl.edu.pw.ii.pik01.seeknresolve.envers.BugEnversMapper;

import java.beans.IntrospectionException;
import java.util.List;

@Service
public class BugRevisionService {

    private BugRepository bugRepository;
    private BugEnversMapper bugEnversMapper = new BugEnversMapper();

    @Autowired
    public BugRevisionService(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    public List<RevisionDiffDTO> getAllForBug(String bugTag) throws IntrospectionException, IllegalAccessException {
        List<Revision<Integer, Bug>> revisions = Lists.newArrayList(bugRepository.getAllRevisions(bugTag));
        return bugEnversMapper.diff(revisions);
    }
}
