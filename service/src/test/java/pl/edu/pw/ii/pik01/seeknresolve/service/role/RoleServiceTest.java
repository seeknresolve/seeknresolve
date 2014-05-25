package pl.edu.pw.ii.pik01.seeknresolve.service.role;

import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RoleDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.ProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Role;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        roleService = new RoleService(roleRepository);
    }

    @Test
    public void testGetOnlyUserRoles() {
        when(roleRepository.findAll()).thenReturn(ImmutableSet.of(userRole("user_role_1"),
                userRole("user_role_2"), projectRole("pr_role")));

        assertThat(roleService.getAllUserRoles())
                .hasSize(2)
                .doNotHave(new Condition<RoleDTO>() {
                    @Override
                    public boolean matches(RoleDTO value) {
                        return value.getRoleName().equals("pr_role");
                    }
                });
    }

    private Role userRole(String roleName) {
        Role role = new UserRole();
        role.setRoleName(roleName);
        return role;
    }

    private Role projectRole(String roleName) {
        Role role = new ProjectRole();
        role.setRoleName(roleName);
        return role;
    }
}
