package org.seeknresolve.controller.bug;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.repository.*;
import org.seeknresolve.domain.test.builders.BugBuilder;
import org.seeknresolve.service.bug.BugService;
import org.seeknresolve.service.bug.generator.BugTagGenerator;
import org.seeknresolve.service.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BugControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BugRepository bugRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserProjectRoleRepository userProjectRoleRepository;

    @Mock
    private BugTagGenerator bugTagGenerator;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        BugService bugService = new BugService(bugRepository, projectRepository,
                userRepository, commentRepository, userProjectRoleRepository, bugTagGenerator);
        BugController bugController = new BugController(bugService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(bugController).build();
        objectMapper = new ObjectMapper();
    }

    private ResultMatcher contentIsJson() {
        return content().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void shouldReturn200AndBugData() throws Exception {
        givenReturningTagData("TTT-1");

        mockMvc.perform(get("/bug/details/TTT-1")).
                andExpect(status().isOk()).
                andExpect(contentIsJson()).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.object.tag").value("TTT-1"));
    }

    private void givenReturningTagData(String tag) {
        Bug bug = new BugBuilder().withTag(tag).build();
        when(bugRepository.findOne(tag)).thenReturn(bug);
    }

    @Test
    public void shouldReturn200AndDeleteTag() throws Exception {
        givenDeletingBugExists("TTT-3");

        mockMvc.perform(delete("/bug/TTT-3")).
                andExpect(status().isOk()).
                andExpect(contentIsJson()).
                andExpect(jsonPath("$.status").value("DELETED")).
                andExpect(jsonPath("$.object").value("TTT-3"));
    }

    private void givenDeletingBugExists(String tag) {
        when(bugRepository.exists(tag)).thenReturn(true);
    }
}
