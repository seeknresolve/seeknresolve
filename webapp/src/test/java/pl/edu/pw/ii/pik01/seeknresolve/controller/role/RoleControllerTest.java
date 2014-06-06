package pl.edu.pw.ii.pik01.seeknresolve.controller.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.ChangeRolePermissionDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Permission;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.PermissionRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.role.RoleService;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @Before
    public void setUp() {
        RoleController roleController = new RoleController(roleService);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void shouldReturn404ForNonExistingRole() throws Exception {
        when(roleService.getRole(anyString())).thenThrow(new EntityNotFoundException("Role not founds"));

        mockMvc.perform(get("/role/details/NonExistingRole")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void shouldReturn200AndRoleDate() throws Exception {
        when(roleService.getRole("role1")).thenReturn(DtosFactory.createRoleDTO(new UserRole("role1")));

        mockMvc.perform(get("/role/details/role1")).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.object.roleName").value("role1"));
    }

    @Test
    public void shouldAddPermissionFromRole() throws Exception {
        ChangeRolePermissionDTO add = new ChangeRolePermissionDTO("admin", "add");
        String params = new ObjectMapper().writeValueAsString(add);

        mockMvc.perform(post("/role/addPermission").contentType(MediaType.APPLICATION_JSON).content(params)).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.status").value("CREATED")).
                andExpect(jsonPath("$.object.roleName").value("admin")).
                andExpect(jsonPath("$.object.permissionName").value("add"));
    }

    @Test
    public void shouldDeletePermissionFromRole() throws Exception {
        ChangeRolePermissionDTO delete = new ChangeRolePermissionDTO("admin", "delete");
        String params = new ObjectMapper().writeValueAsString(delete);

        mockMvc.perform(post("/role/deletePermission").contentType(MediaType.APPLICATION_JSON).content(params)).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.status").value("DELETED")).
                andExpect(jsonPath("$.object.roleName").value("admin")).
                andExpect(jsonPath("$.object.permissionName").value("delete"));
    }
}
