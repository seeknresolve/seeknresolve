package org.seeknresolve.domain.repository;

import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.repository.search.SearchWithAuditRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, SearchWithAuditRepository<User, Long> {
    User findOneByLogin(String login);
    User findOneByEmail(String email);
    @Query("select u from User u where u not in (select up from User up, UserProjectRole upr where upr.user.id = up.id and upr.project.id = :projectId)")
    List<User> findNotAssignedToProject(@Param("projectId") Long projectId);
}
