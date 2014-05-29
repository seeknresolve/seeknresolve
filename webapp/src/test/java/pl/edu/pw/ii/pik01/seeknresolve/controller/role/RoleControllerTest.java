package pl.edu.pw.ii.pik01.seeknresolve.controller.role;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.role.RoleService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        RoleService roleService = new RoleService(roleRepository);
        RoleController roleController = new RoleController(roleService);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void shouldReturn404ForNonExistingRole() throws Exception {
        mockMvc.perform(get("/role/NonExistingRole")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void shouldReturn200AndRoleDate() throws Exception {
        when(roleRepository.findOne("role1")).thenReturn(new UserRole("role1"));

        mockMvc.perform(get("/role/role1")).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.object.roleName").value("role1"));
    }
}
