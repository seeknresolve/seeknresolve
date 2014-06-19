package org.seeknresolve.service.comment;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.seeknresolve.TestAppContext;
import org.seeknresolve.domain.dto.CommentDTO;
import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.domain.repository.BugRepository;
import org.seeknresolve.domain.repository.ProjectRepository;
import org.seeknresolve.domain.repository.RoleRepository;
import org.seeknresolve.domain.repository.UserRepository;
import org.seeknresolve.domain.test.builders.BugBuilder;
import org.seeknresolve.domain.test.builders.ProjectBuilder;
import org.seeknresolve.domain.test.builders.UserBuilder;
import org.seeknresolve.service.security.ContextUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestAppContext.class)
@ActiveProfiles("test")
public class CommentServiceIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private CommentService commentService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldNotAllowToCommentBugWhenNoPermissionForItsProject() {
        //given:
        User user = createAndSaveUser("rnwTestOnly");
        userLogged(user);
        Project project = createAndSaveProject("projectTest" + System.currentTimeMillis());
        Bug bug = createAndSaveBug("TTT-1", user, project);
        CommentDTO comment = givenCommentDTO(user, bug);
        //when:
        expectedException.expect(SecurityException.class);
        commentService.createAndSaveNewComment(comment, user);
    }

    private Project createAndSaveProject(String name) {
        Project project = new ProjectBuilder().withName(name).withDescription("Description").withTag("TTT").build();
        return projectRepository.save(project);
    }

    private void userLogged(final User user) {
        SecurityContextHolder.getContext().setAuthentication(new AbstractAuthenticationToken(Lists.newArrayList()) {
            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return new ContextUser(user);
            }
        });
    }

    private User createAndSaveUser(String login) {
        User user = new UserBuilder().withLogin(login)
                .withFirstName("R").withLastName("N")
                .withEmail("r@r.pl").withRole(createAndSaveRole("TEST_USER"))
                .build();
        user.setPassword("abcd");
        return userRepository.save(user);
    }

    private UserRole createAndSaveRole(String roleName) {
        UserRole userRole = new UserRole(roleName);
        return roleRepository.save(userRole);
    }

    private Bug createAndSaveBug(String tag, User user, Project project) {
        Bug bug = new BugBuilder().withTag(tag).withName("Name").withDescription("Description").withAssignee(user).withReporter(user).withProject(project).build();
        return bugRepository.save(bug);
    }

    private CommentDTO givenCommentDTO(User user, Bug bug) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthorLogin(user.getLogin());
        commentDTO.setAuthorId(user.getId());
        commentDTO.setBugTag(bug.getTag());
        commentDTO.setContent("test");
        return commentDTO;
    }
}
