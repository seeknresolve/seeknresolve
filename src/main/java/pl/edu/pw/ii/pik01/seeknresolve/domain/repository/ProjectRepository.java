package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.search.CustomRepository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>, CustomRepository<Project, Long> {
}
