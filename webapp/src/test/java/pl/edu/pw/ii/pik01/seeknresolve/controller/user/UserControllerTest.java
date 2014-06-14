package pl.edu.pw.ii.pik01.seeknresolve.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pw.ii.pik01.seeknresolve.domain.common.test.builders.UserBuilder;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ChangePasswordDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.CreateUserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.user.UserService;

import javax.persistence.EntityNotFoundException;

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
    private UserService userService;

    @Before
    public void setUp() {
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldReturn404ForNonExistingUser() throws Exception {
        when(userService.findByLogin(anyString())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/user/details/NonExistingUser")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturn200AndUserData() throws Exception {
        User existingUser = new UserBuilder().withLogin("cthulu")
                                                .withFirstName("Al-Khadhulu")
                                                .withLastName("Kuthulu")
                                                .withEmail("cthulu@worldDestroyer.xx")
                                                .withRole(new UserRole("God"))
                                                .build();

        when(userService.findByLogin("cthulu")).thenReturn(DtosFactory.createUserDTO(existingUser));

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

        when(userService.createAndSaveNewUser(any())).thenAnswer(Return.firstParameter());

        mockMvc.perform(post("/user/create").
                    contentType(MediaType.APPLICATION_JSON).
                    content(asJson(createUserDTO))).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.status").value("CREATED")).
                andExpect(jsonPath("$.object.login").value("AwasomeLogin"));
    }

    @Test
    public void testChangePassword() throws Exception {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO("mdz", "qwerty", "qwerty");

        mockMvc.perform(post("/user/changePassword").
                    contentType(MediaType.APPLICATION_JSON).
                    content(asJson(changePasswordDTO))).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.status").value("UPDATED"));
    }

    private String asJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private static class Return<T> implements Answer<T> {

        private int paramNumber = 0;

        public static <Z> Return<Z> firstParameter() {
            Return<Z> r = new Return<>();
            r.paramNumber = 0;
            return r;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T answer(InvocationOnMock invocation) throws Throwable {
            return (T) invocation.getArguments()[paramNumber];
        }
    }
}
