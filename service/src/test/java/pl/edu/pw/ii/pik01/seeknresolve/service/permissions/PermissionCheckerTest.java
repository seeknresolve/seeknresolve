package pl.edu.pw.ii.pik01.seeknresolve.service.permissions;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.*;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionChecker;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

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
        Permission addUserPermission = fromString("add_user");
        userRole.setPermissions(ImmutableSet.of(addUserPermission));

        User user = new User();
        user.setUserRole(userRole);

        when(userService.getLoggedUser()).thenReturn(user);

        assertTrue(permissionChecker.hasPermission(addUserPermission));
        assertFalse(permissionChecker.hasPermission(fromString("remove_user")));
    }

    @Test
    public void testCheckProjectPermission() {
        ProjectRole projectRole = new ProjectRole();
        Permission createBugPermission = fromString("create_bug");
        projectRole.setPermissions(ImmutableSet.of(createBugPermission));

        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setProjectRole(projectRole);

        when(userProjectRoleRepository.findByUserAndProject(any(), any())).thenReturn(userProjectRole);

        assertTrue(permissionChecker.hasProjectPermission(new Project(), createBugPermission));
        assertFalse(permissionChecker.hasProjectPermission(new Project(), fromString("remove_bug")));
    }

    private Permission fromString(String textualPermission) {
        Permission permission = new Permission();
        permission.setPermission(textualPermission);
        return permission;
    }
}
