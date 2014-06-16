package pl.edu.pw.ii.pik01.seeknresolve.service.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailServiceImplTest {
    private static final String USERNAME = "john";

    @Mock
    private UserRepository userRepository;

    @Test(expected = NullPointerException.class)
    public void failOnNullUsername() {
        UserDetailsService userDetailService = new UserDetailServiceImpl(userRepository);
        userDetailService.loadUserByUsername(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void failOnEmptyUsername() {
        UserDetailsService userDetailService = new UserDetailServiceImpl(userRepository);
        userDetailService.loadUserByUsername("");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void failOnNotExistingUser() {
        UserDetailsService userDetailService = new UserDetailServiceImpl(userRepository);
        userDetailService.loadUserByUsername(USERNAME);
    }

    @Test
    public void pass() {
        UserRole userRole = new UserRole();
        userRole.setRoleName("ADMIN");

        User user = new User();
        user.setUserRole(userRole);
        user.setLogin(USERNAME);
        user.setPassword("pass");

        when(userRepository.findOneByLogin(USERNAME)).thenReturn(user);

        UserDetailsService userDetailService = new UserDetailServiceImpl(userRepository);
        UserDetails userDetails = userDetailService.loadUserByUsername(USERNAME);

        assertTrue(userDetails.getUsername().equals(USERNAME));
        assertTrue(userDetails instanceof ContextUser);
        assertTrue(((ContextUser)userDetails).getUser() == user);
        verify(userRepository, times(1)).findOneByLogin(USERNAME);
    }
}
