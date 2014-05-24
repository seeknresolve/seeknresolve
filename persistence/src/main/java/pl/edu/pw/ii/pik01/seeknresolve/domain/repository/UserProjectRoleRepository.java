package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;

@Repository
public interface UserProjectRoleRepository extends CrudRepository<UserProjectRole, Long> {
    UserProjectRole findOneByUserAndProject(User user, Project project);
}
