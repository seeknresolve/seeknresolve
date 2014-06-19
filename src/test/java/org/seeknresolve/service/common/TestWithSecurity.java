package org.seeknresolve.service.common;

import com.google.common.collect.Lists;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.entity.ProjectRole;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserProjectRole;
import org.seeknresolve.domain.repository.RoleRepository;
import org.seeknresolve.domain.repository.UserProjectRoleRepository;

import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class TestWithSecurity {
    public UserProjectRoleRepository userProjectRoleRepository = mock(UserProjectRoleRepository.class);
    public RoleRepository roleRepository = mock(RoleRepository.class);

    public void givenNoRolesReturnedForUser(User user) {
        given(userProjectRoleRepository.findByUser(user)).willReturn(Lists.newArrayList());
    }

    public void givenRolesReturnedForUser(User user, Project... projects) {
        given(userProjectRoleRepository.findByUser(user)).willReturn(Lists.newArrayList(
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
