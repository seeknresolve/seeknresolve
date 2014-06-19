package org.seeknresolve.domain.repository;

import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.search.SearchWithAuditRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends CrudRepository<Bug, String>, SearchWithAuditRepository<Bug, String> {
    List<Bug> findByProject(Project project);
}
