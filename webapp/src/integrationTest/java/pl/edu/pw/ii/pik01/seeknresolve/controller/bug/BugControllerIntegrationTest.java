package pl.edu.pw.ii.pik01.seeknresolve.controller.bug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.edu.pw.ii.pik01.seeknresolve.TestAppContext;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = TestAppContext.class)
public class BugControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
    public void shouldReturn404AndErrorWhenTryingToDeleteNotExistingBug() throws Exception {
        mockMvc.perform(delete("/bug/NotExistingBug")).
                andExpect(status().isNotFound()).
                andExpect(contentIsJson()).
                andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void shouldReturn400AndFieldErrorsIfBugIsInvalid() throws Exception {
        String bugAsJson = getBugDTOForSave("", "name");

        mockMvc.perform(post("/bug").contentType(MediaType.APPLICATION_JSON).content(bugAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(contentIsJson())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("tag")))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", equalTo("")));
    }

    private String getBugDTOForSave(String tag, String name) throws JsonProcessingException {
        BugDTO bugDTO = new BugDTO();
        bugDTO.setName(name);
        bugDTO.setDescription("Description");
        bugDTO.setTag(tag);
        return objectMapper.writeValueAsString(bugDTO);
    }

    @Test
    public void shouldReturn400AndFieldErrorsIfBugIsInvalidAndHasManyFieldErrors() throws Exception {
        String bugAsJson = getBugDTOForSave("", "");

        mockMvc.perform(post("/bug").contentType(MediaType.APPLICATION_JSON).content(bugAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(contentIsJson())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)))
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("tag", "name")))
                .andExpect(jsonPath("$.fieldErrors[*].rejectedValue", containsInAnyOrder("", "")));
    }
}