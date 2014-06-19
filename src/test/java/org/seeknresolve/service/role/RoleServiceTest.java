package org.seeknresolve.service.role;

import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.dto.RoleDTO;
import org.seeknresolve.domain.entity.Permission;
import org.seeknresolve.domain.entity.ProjectRole;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.domain.repository.PermissionRepository;
import org.seeknresolve.domain.repository.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Before
    public void setUp() {
        roleService = new RoleService(roleRepository, permissionRepository);
    }

    @Test
    public void testGetOnlyUserRoles() {
        when(roleRepository.findAll()).thenReturn(ImmutableSet.of(new UserRole("user_role_1"),
                new UserRole("user_role_2"), new ProjectRole("pr_role")));

        assertThat(roleService.getAllUserRoles())
                .hasSize(2)
                .doNotHave(new Condition<RoleDTO>() {
                    @Override
                    public boolean matches(RoleDTO value) {
                        return value.getRoleName().equals("pr_role");
                    }
                });
    }

    @Test
    public void testGetOnlyProjectRoles() {
        when(roleRepository.findAll()).thenReturn(ImmutableSet.of(new UserRole("user_role_1"),
                new UserRole("user_role_2"), new ProjectRole("pr_role")));

        assertThat(roleService.getAllProjectRoles())
                .hasSize(1)
                .has(new Condition<RoleDTO>() {
                    @Override
                    public boolean matches(RoleDTO value) {
                        return value.getRoleName().equals("pr_role");
                    }
                }, Index.atIndex(0));
    }

    @Test
    public void testAddPermissionToRole() {
        UserRole adminRole = new UserRole("admin");
        adminRole.getPermissions().add(new Permission("add"));

        when(roleRepository.findOne("admin")).thenReturn(adminRole);

        Permission deletePermission = new Permission("delete");
        when(permissionRepository.findOne("delete")).thenReturn(deletePermission);

        roleService.addPermissionToRole("admin", "delete");

        assertThat(adminRole.getPermissions()).
                contains(new Permission("add")).
                contains(new Permission("delete"));

        verify(roleRepository, times(1)).save(adminRole);
    }

    @Test
    public void testDeletePermissionToRole() {
        UserRole adminRole = new UserRole("admin");
        adminRole.getPermissions().add(new Permission("add"));
        adminRole.getPermissions().add(new Permission("delete"));

        when(roleRepository.findOne("admin")).thenReturn(adminRole);

        Permission deletePermission = new Permission("delete");
        when(permissionRepository.findOne("delete")).thenReturn(deletePermission);

        roleService.deletePermissionFromRole("admin", "delete");

        assertThat(adminRole.getPermissions()).
                contains(new Permission("add")).
                doesNotContain(new Permission("delete"));

        verify(roleRepository, times(1)).save(adminRole);
    }
}
