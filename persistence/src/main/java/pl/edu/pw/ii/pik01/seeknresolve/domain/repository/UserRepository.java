package pl.edu.pw.ii.pik01.seeknresolve.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByLogin(String login);
}
