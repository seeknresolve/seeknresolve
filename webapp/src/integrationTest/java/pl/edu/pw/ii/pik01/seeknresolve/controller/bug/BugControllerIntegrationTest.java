package pl.edu.pw.ii.pik01.seeknresolve.controller.bug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.edu.pw.ii.pik01.seeknresolve.TestAppContext;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.BugDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.*;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.PermissionsConstants;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.RolesConstants;
import pl.edu.pw.ii.pik01.seeknresolve.service.security.ContextUser;
import pl.edu.pw.ii.pik01.seeknresolve.test.TestEntityFactory;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = TestAppContext.class)
public class BugControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityFactory testEntityFactory;

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

    @Test
    public void shouldUpdateBugWithCorrectUpdateData() throws Exception {
        User user = userCreatedAndLogged("rnwTestOnly");
        Project project = testEntityFactory.createAndSaveProject("projectTest" + System.currentTimeMillis(), "TEST2");
        Bug bug = testEntityFactory.createAndSaveBug("TTT-1", user, project);
        testEntityFactory.grantUserProjectRoleWithPermissions(user, project, RolesConstants.DEVELOPER_ROLE_NAME,
                PermissionsConstants.PROJECT_EVERYTHING);

        String newName = "newName";
        String updateBugDTO = createUpdateBugDTO(newName, bug);

        mockMvc.perform(put("/bug").contentType(MediaType.APPLICATION_JSON).content(updateBugDTO))
                .andExpect(status().isOk())
                .andExpect(contentIsJson());
    }

    private User userCreatedAndLogged(String login) {
        User user = testEntityFactory.createAndSaveUser(login);
        userLogged(user);
        return user;
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

    private String createUpdateBugDTO(String newName, Bug bug) throws JsonProcessingException {
        BugDTO bugDTO = DtosFactory.createBugDTO(bug);
        bugDTO.setName(newName);
        return objectMapper.writeValueAsString(bugDTO);
    }

    @Test
    public void shouldReturnFieldErrorsWhenBugIsUpdatedWithIncorrectData() throws Exception {
        User user = userCreatedAndLogged("rnwTestOnly");
        Project project = testEntityFactory.createAndSaveProject("projectTest" + System.currentTimeMillis(), "TEST1");
        Bug bug = testEntityFactory.createAndSaveBug("TEST1-1", user, project);
        testEntityFactory.grantUserProjectRoleWithPermissions(user, project, RolesConstants.DEVELOPER_ROLE_NAME,
                PermissionsConstants.PROJECT_EVERYTHING);

        String newName = "";
        String updateBugDTO = createUpdateBugDTO(newName, bug);

        mockMvc.perform(put("/bug").contentType(MediaType.APPLICATION_JSON).content(updateBugDTO))
                .andExpect(status().isBadRequest())
                .andExpect(contentIsJson())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("name")))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", equalTo("")));
    }
}