package org.seeknresolve.domain.repository;

import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.search.CustomRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>, CustomRepository<Project, Long> {
}
