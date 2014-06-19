package org.seeknresolve.domain.repository;

import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.search.SearchWithAuditRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>, SearchWithAuditRepository<Project, Long> {
}
