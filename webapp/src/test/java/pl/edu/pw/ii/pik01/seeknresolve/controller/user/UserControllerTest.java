package pl.edu.pw.ii.pik01.seeknresolve.controller.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.UserBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldReturn404ForNonExistingUser() throws Exception {
        mockMvc.perform(get("/user/NonExistingUser")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void shouldReturn200AndUserData() throws Exception {
        User existingUser = new UserBuilder().withLogin("cthulu")
                                                .withFirstName("Al-Khadhulu")
                                                .withLastName("Kuthulu")
                                                .withEmail("cthulu@worldDestroyer.xx")
                                                .withRole(new UserRole("God"))
                                                .build();
        when(userRepository.findOneByLogin(anyString())).thenReturn(existingUser);

        mockMvc.perform(get("/user/cthulu")).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.object.login").value("cthulu")).
                andExpect(jsonPath("$.object.firstName").value("Al-Khadhulu")).
                andExpect(jsonPath("$.object.lastName").value("Kuthulu")).
                andExpect(jsonPath("$.object.email").value("cthulu@worldDestroyer.xx")).
                andExpect(jsonPath("$.object.userRole").value("God"));
    }
}
