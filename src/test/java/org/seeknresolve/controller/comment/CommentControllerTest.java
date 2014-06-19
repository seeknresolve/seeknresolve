package org.seeknresolve.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.repository.BugRepository;
import org.seeknresolve.domain.repository.CommentRepository;
import org.seeknresolve.domain.repository.UserRepository;
import org.seeknresolve.service.comment.CommentService;
import org.seeknresolve.service.user.UserService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CommentControllerTest {
    private MockMvc mockMvc;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BugRepository bugRepository;

    @Mock
    UserService userService;

    private ObjectMapper objectMapper;

    @Before
    public void setUp(){
        CommentService commentService = new CommentService(commentRepository, userRepository, bugRepository, userService);
        CommentController commentController = new CommentController(commentService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void dummyTest() {
        assertThat(true).isEqualTo(true);
    }
}
