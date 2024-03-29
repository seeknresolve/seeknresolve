package org.seeknresolve.controller.role;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.dto.ChangeRolePermissionDTO;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.service.common.DtosFactory;
import org.seeknresolve.service.role.RoleService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void shouldReturn200AndRoleDate() throws Exception {
        when(roleService.getRole("role1")).thenReturn(DtosFactory.createRoleDTO(new UserRole("role1")));

        mockMvc.perform(get("/role/details/role1")).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.object.roleName").value("role1"));
    }

    @Test
    public void shouldAddPermissionToRole() throws Exception {
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
