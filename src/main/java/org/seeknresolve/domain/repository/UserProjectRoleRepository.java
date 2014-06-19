package org.seeknresolve.domain.repository;

import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserProjectRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectRoleRepository extends CrudRepository<UserProjectRole, Long> {
    UserProjectRole findByUserAndProject(User user, Project project);
    List<UserProjectRole> findByUser(User user);
    @Query("select upr from UserProjectRole upr where upr.project.id = :projectId")
    List<UserProjectRole> findByProjectId(@Param("projectId") Long projectId);
}
