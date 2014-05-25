package pl.edu.pw.ii.pik01.seeknresolve.service.common;

import com.google.common.collect.Lists;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.ProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;

import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestWithSecurity {
    public UserProjectRoleRepository userProjectRoleRepository = mock(UserProjectRoleRepository.class);

    public void givenNoRolesReturnedForUser(User user) {
        when(userProjectRoleRepository.findByUser(user)).thenReturn(Lists.newArrayList());
    }

    public void givenRolesReturnedForUser(User user, Project... projects) {
        when(userProjectRoleRepository.findByUser(user)).thenReturn(Lists.newArrayList(
                Lists.newArrayList(projects).stream()
                        .map(project -> createUserProjectRole(user, project))
                        .collect(Collectors.toList())
        ));
    }

    private UserProjectRole createUserProjectRole(User user, Project project) {
        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setProject(project);
        userProjectRole.setUser(user);
        ProjectRole projectRole = new ProjectRole();
        userProjectRole.setProjectRole(projectRole);
        return userProjectRole;
    }
}
