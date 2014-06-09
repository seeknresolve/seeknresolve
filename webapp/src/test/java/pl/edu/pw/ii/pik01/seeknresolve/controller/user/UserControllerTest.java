package pl.edu.pw.ii.pik01.seeknresolve.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.UserBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.CreateUserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;
import pl.edu.pw.ii.pik01.seeknresolve.test.Return;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProjectRoleRepository userProjectRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        UserService userService = new UserService(userRepository, userProjectRoleRepository, roleRepository);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldReturn404ForNonExistingUser() throws Exception {
        mockMvc.perform(get("/user/details/NonExistingUser")).
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

        mockMvc.perform(get("/user/details/cthulu")).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.object.login").value("cthulu")).
                andExpect(jsonPath("$.object.firstName").value("Al-Khadhulu")).
                andExpect(jsonPath("$.object.lastName").value("Kuthulu")).
                andExpect(jsonPath("$.object.email").value("cthulu@worldDestroyer.xx")).
                andExpect(jsonPath("$.object.userRole").value("God"));
    }

    @Test
    public void shouldCreateUser() throws Exception {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setLogin("AwasomeLogin");
        createUserDTO.setPassword("Password");
        createUserDTO.setUserRole("USER");

        when(roleRepository.findOne("USER")).thenReturn(new UserRole("USER"));
        when(userRepository.save(any(User.class))).thenAnswer(Return.firstParameter());

        mockMvc.perform(post("/user/create").
                    contentType(MediaType.APPLICATION_JSON).
                    content(userAsJson(createUserDTO))).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.status").value("CREATED")).
                andExpect(jsonPath("$.object.login").value("AwasomeLogin"));
    }

    private String userAsJson(UserDTO userDTO) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(userDTO);
    }
}
