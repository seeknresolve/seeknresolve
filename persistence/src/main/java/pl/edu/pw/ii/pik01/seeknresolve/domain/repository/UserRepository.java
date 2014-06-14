package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByLogin(String login);
    User findOneByEmail(String email);
    @Query("select u from User u where u not in (select up from User up, UserProjectRole upr where upr.user.id = up.id and upr.project.id = :projectId)")
    List<User> findNotAssignedToProject(@Param("projectId") Long projectId);
}
