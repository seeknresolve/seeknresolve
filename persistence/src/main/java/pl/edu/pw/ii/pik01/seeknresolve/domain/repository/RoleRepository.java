package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
}
