package org.seeknresolve.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.seeknresolve.domain.dto.ChangePasswordDTO;
import org.seeknresolve.domain.dto.CreateUserDTO;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.domain.test.builders.UserBuilder;
import org.seeknresolve.service.common.DtosFactory;
import org.seeknresolve.service.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
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
    public void shouldReturn200AndUserData() throws Exception {
        User existingUser = new UserBuilder().withLogin("cthulu")
                                                .withFirstName("Al-Khadhulu")
                                                .withLastName("Kuthulu")
                                                .withEmail("cthulu@worldDestroyer.xx")
                                                .withRole(new UserRole("God"))
                                                .build();

        when(userService.findByLogin("cthulu")).thenReturn(DtosFactory.createUserDetailsDTO(existingUser));

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
