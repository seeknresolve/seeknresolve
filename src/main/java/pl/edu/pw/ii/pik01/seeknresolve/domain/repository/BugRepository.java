package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.search.CustomRepository;

import java.util.List;

@Repository
public interface BugRepository extends CrudRepository<Bug, String>, CustomRepository<Bug, String> {
    List<Bug> findByProject(Project project);
}
