package pl.edu.pw.ii.pik01.seeknresolve.controller.bug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
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
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.PermissionsConstants;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.RolesConstants;
import pl.edu.pw.ii.pik01.seeknresolve.service.security.ContextUser;
import pl.edu.pw.ii.pik01.seeknresolve.test.TestEntityFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = TestAppContext.class)
@ActiveProfiles("test")
public class BugControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestEntityFactory testEntityFactory;

    @Autowired
    private ProjectRepository projectRepository;

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
        User user = userCreatedAndLogged("rnwTestOnly1");
        Project project = testEntityFactory.createAndSaveProject("projectTest" + System.currentTimeMillis(), "TEST1");
        String bugAsJson = createBugDTOForSave("", user, project);

        mockMvc.perform(post("/bug").contentType(MediaType.APPLICATION_JSON).content(bugAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(contentIsJson())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("name")))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", equalTo("")));
    }

    @Test
    public void shouldReturn400AndFieldErrorsIfBugIsInvalidAndHasManyFieldErrors() throws Exception {
        String bugAsJson = createBugDTOForSave("", null, null);

        mockMvc.perform(post("/bug").contentType(MediaType.APPLICATION_JSON).content(bugAsJson))
                .andExpect(status().isBadRequest())
                .andExpect(contentIsJson())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors[*].field", containsInAnyOrder("reporterId", "projectId", "name")));
    }

    @Test
    public void shouldUpdateBugWithCorrectUpdateData() throws Exception {
        User user = userCreatedAndLogged("rnwTestOnly2");
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
        User user = userCreatedAndLogged("rnwTestOnly3");
        Project project = testEntityFactory.createAndSaveProject("projectTest" + System.currentTimeMillis(), "TEST3");
        Bug bug = testEntityFactory.createAndSaveBug("TEST1-1", user, project);
        testEntityFactory.grantUserProjectRoleWithPermissions(user, project, RolesConstants.DEVELOPER_ROLE_NAME,
                PermissionsConstants.PROJECT_EVERYTHING);

        String newName = "";
        String updateBugDTO = createUpdateBugDTO(newName, bug);

        mockMvc.perform(put("/bug").contentType(MediaType.APPLICATION_JSON).content(updateBugDTO))
                .andExpect(status().isBadRequest())
                .andExpect(contentIsJson())
                .andExpect(jsonPath("$.fieldErrors").exists())
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("name")))
                .andExpect(jsonPath("$.fieldErrors[0].rejectedValue", equalTo("")));
    }

    @Test
    public void shouldSaveNewBugAndIncrementLastBugNumberInProject() throws Exception {
        User user = userCreatedAndLogged("rnwTestOnly4");
        Project project = testEntityFactory.createAndSaveProject("projectTest" + System.currentTimeMillis(), "TEST4");
        testEntityFactory.grantUserProjectRoleWithPermissions(user, project, RolesConstants.DEVELOPER_ROLE_NAME,
                PermissionsConstants.PROJECT_EVERYTHING);

        String bugName = "RandomName" + RandomStringUtils.randomAlphabetic(5);
        String createBugDTO = createBugDTOForSave(bugName, user, project);

        mockMvc.perform(post("/bug").contentType(MediaType.APPLICATION_JSON).content(createBugDTO))
                .andExpect(status().isOk())
                .andExpect(contentIsJson())
                .andExpect(jsonPath("$.object").exists())
                .andExpect(jsonPath("$.object.tag").value("TEST4-1"))
                .andExpect(jsonPath("$.object.name").value(bugName));

        thenLastBugNumberInProjectIncremented(project);
    }

    private String createBugDTOForSave(String name, User user, Project project) throws JsonProcessingException {
        BugDTO bugDTO = new BugDTO();
        bugDTO.setName(name);
        bugDTO.setDescription("");
        bugDTO.setReporterId(user != null ? user.getId() : null);
        bugDTO.setProjectId(user != null ? project.getId() : null);
        bugDTO.setPriority(Bug.Priority.NORMAL);
        return objectMapper.writeValueAsString(bugDTO);
    }

    private void thenLastBugNumberInProjectIncremented(Project project) {
        Project savedProject = projectRepository.findOne(project.getId());
        assertThat(savedProject.getId()).isEqualTo(project.getId());
        assertThat(savedProject.getName()).isEqualTo(project.getName());
        assertThat(savedProject.getLastBugNumber()).isEqualTo(project.getLastBugNumber() + 1);
    }
}