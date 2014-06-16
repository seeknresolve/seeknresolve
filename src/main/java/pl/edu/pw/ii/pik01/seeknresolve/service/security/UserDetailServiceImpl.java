package pl.edu.pw.ii.pik01.seeknresolve.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        checkNotNull(username);

        if(isEmpty(username)) {
            throw new UsernameNotFoundException("Username cannot be empty");
        }

        User user = userRepository.findOneByLogin(username);
        if(user == null) {
            throw new UsernameNotFoundException("User " + username + " doesn't exists");
        }

        return new ContextUser(user);
    }
}
