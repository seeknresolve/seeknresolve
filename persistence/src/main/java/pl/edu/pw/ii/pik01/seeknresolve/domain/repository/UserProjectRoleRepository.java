package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;

import java.util.List;

@Repository
public interface UserProjectRoleRepository extends CrudRepository<UserProjectRole, Long> {
    UserProjectRole findByUserAndProject(User user, Project project);
    List<UserProjectRole> findByUser(User user);
}
