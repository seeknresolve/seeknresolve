package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.UserProjectRole;

public interface UserProjectRoleRepository extends CrudRepository<UserProjectRole, Long> {

    UserProjectRole findOneByUserAndProject(User user, Project project);

}
