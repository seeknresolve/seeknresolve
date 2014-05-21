package pl.edu.pw.ii.pik01.seeknresolve.service.permissions;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionChecker;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        userRole.setPermissions(ImmutableSet.of(fromString("add_user")));

        User user = new User();
        user.setUserRole(userRole);

        when(userService.getLoggedUser()).thenReturn(user);

        assertTrue(permissionChecker.hasPermission("add_user"));
        assertFalse(permissionChecker.hasPermission("remove_user"));
    }

    @Test
    public void testCheckProjectPermission() {
        ProjectRole projectRole = new ProjectRole();
        projectRole.setPermissions(ImmutableSet.of(fromString("create_bug")));

        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setProjectRole(projectRole);

        when(userProjectRoleRepository.findOneByUserAndProject(any(), any())).thenReturn(userProjectRole);

        assertTrue(permissionChecker.hasProjectPermission(new Project(), "create_bug"));
        assertFalse(permissionChecker.hasProjectPermission(new Project(), "remove_bug"));
    }

    private Permission fromString(String textualPermission) {
        Permission permission = new Permission();
        permission.setPermission(textualPermission);
        return permission;
    }
}
