package pl.edu.pw.ii.pik01.seeknresolve.controller.bug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.BugBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.*;
import pl.edu.pw.ii.pik01.seeknresolve.service.bug.BugService;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        BugService bugService = new BugService(bugRepository, projectRepository,
                userRepository, commentRepository, userProjectRoleRepository);
        BugController bugController = new BugController(bugService, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(bugController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturn404ForNotExistingBugWithErrorMessageInResponse() throws Exception {
        mockMvc.perform(get("/bug/details/TotallyNotExistingBug")).
                andExpect(status().isNotFound()).
                andExpect(contentIsJson()).
                andExpect(jsonPath("$.error").exists());
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
    public void shouldReturn404AndErrorWhenTryingToDeleteNotExistingBug() throws Exception {
        mockMvc.perform(delete("/bug/NotExistingBug")).
                andExpect(status().isNotFound()).
                andExpect(contentIsJson()).
                andExpect(jsonPath("$.error").exists());
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

    @Test
    public void shouldCreateNewBugAndReturnResponse() throws Exception {
        String tag = "TTT-10";
        String bugAsJson = getBugDTOForSave(tag);
        givenBugWasCreated(tag);

        mockMvc.perform(post("/bug").contentType(MediaType.APPLICATION_JSON).content(bugAsJson)).
                andExpect(status().isOk()).
                andExpect(contentIsJson()).
                andExpect(jsonPath("$.status").value("CREATED")).
                andExpect(jsonPath("$.object.tag").value(tag));
    }

    private void givenBugWasCreated(String tag) {
        when(bugRepository.save(any(Bug.class))).thenReturn(new BugBuilder().withTag(tag).build());
    }

    private String getBugDTOForSave(String tag) throws JsonProcessingException {
        BugDTO bugDTO = new BugDTO();
        bugDTO.setTag(tag);
        return objectMapper.writeValueAsString(bugDTO);
    }

    @Test
    public void shouldReturn400IfSaveWasUnsuccessful() throws Exception {
        givenBugWasNotCreated();

        mockMvc.perform(post("/bug").contentType(MediaType.APPLICATION_JSON).content(getBugDTOForSave("T"))).
                andExpect(status().isBadRequest()).
                andExpect(contentIsJson()).
                andExpect(jsonPath("$.error").exists());
    }

    private void givenBugWasNotCreated() {
        when(bugRepository.save(any(Bug.class))).thenReturn(null);
    }
}
