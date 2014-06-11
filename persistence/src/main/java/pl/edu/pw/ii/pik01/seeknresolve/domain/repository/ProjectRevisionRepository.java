package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.envers.repository.support.EnversRevisionRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;

@Repository
public interface ProjectRevisionRepository extends EnversRevisionRepository<Project, Long, Long> {
}
