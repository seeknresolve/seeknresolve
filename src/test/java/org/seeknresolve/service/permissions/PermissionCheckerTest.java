package org.seeknresolve.service.permissions;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.entity.*;
import org.seeknresolve.domain.repository.UserProjectRoleRepository;
import org.seeknresolve.service.permission.PermissionChecker;
import org.seeknresolve.service.user.UserService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionCheckerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserProjectRoleRepository userProjectRoleRepository;

    private PermissionChecker permissionChecker;

    @Before
    public void setUp() {
        permissionChecker = new PermissionChecker(userService, userProjectRoleRepository);
    }

    @Test
    public void testCheckUserPermission() {
        UserRole userRole = new UserRole();
        Permission addUserPermission = new Permission("add_user");
        userRole.setPermissions(ImmutableSet.of(addUserPermission));

        User user = new User();
        user.setUserRole(userRole);

        when(userService.getLoggedUser()).thenReturn(user);

        assertTrue(permissionChecker.hasPermission(addUserPermission));
        assertFalse(permissionChecker.hasPermission(new Permission("remove_user")));
    }

    @Test
    public void testCheckProjectPermission() {
        ProjectRole projectRole = new ProjectRole();
        Permission createBugPermission = new Permission("create_bug");
        projectRole.setPermissions(ImmutableSet.of(createBugPermission));

        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setProjectRole(projectRole);

        when(userProjectRoleRepository.findByUserAndProject(any(), any())).thenReturn(userProjectRole);

        assertTrue(permissionChecker.hasProjectPermission(new Project(), createBugPermission));
        assertFalse(permissionChecker.hasProjectPermission(new Project(), new Permission("remove_bug")));
    }
}
