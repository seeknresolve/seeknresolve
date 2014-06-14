package pl.edu.pw.ii.pik01.seeknresolve.service.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.UserBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ChangePasswordDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProjectRoleRepository userProjectRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(userRepository, userProjectRoleRepository, roleRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangePasswordWithDifferentConfirm() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("userLogin", "password", "differentPassword");
        userService.changePassword(changePasswordDTO);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testChangePasswordWithNonExistingUser() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("userLogin", "password", "password");
        userService.changePassword(changePasswordDTO);
    }

    @Test
    public void testChangePassword() {
        User user = new UserBuilder().withLogin("userLogin").withFirstName("Adam").withLastName("Miałczyński").build();
        user.setPassword("pass");

        when(userRepository.findOneByLogin("userLogin")).thenReturn(user);

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("userLogin", "password", "password");
        userService.changePassword(changePasswordDTO);

        verify(userRepository, times(1)).findOneByLogin("userLogin");
        verify(userRepository, times(1)).save(user);
        assertThat(user.getPassword()).isNotEqualTo("pass");
    }
}
