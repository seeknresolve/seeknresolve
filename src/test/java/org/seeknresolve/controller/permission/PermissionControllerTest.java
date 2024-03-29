package org.seeknresolve.controller.permission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.repository.PermissionRepository;
import org.seeknresolve.domain.repository.RoleRepository;
import org.seeknresolve.service.permission.PermissionChecker;
import org.seeknresolve.service.permission.PermissionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class PermissionControllerTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionChecker permissionChecker;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        PermissionService permissionService = new PermissionService(permissionRepository, roleRepository);
        PermissionController permissionController = new PermissionController(permissionService, permissionChecker);
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();
    }

    @Test
    public void shouldReturn404ForNonExistingPermission() throws Exception {
        mockMvc.perform(get("/permission/NonExistingPermission")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void shouldReturn200AndDeletedPermissionName() throws Exception {
        when(permissionRepository.exists(anyString())).thenReturn(true);

        mockMvc.perform(delete("/permission/permName")).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").doesNotExist()).
                andExpect(jsonPath("$.object").value("permName"));
    }

    @Test
    public void shouldReturnWhileDeletingNonExistingPermission() throws Exception {
        mockMvc.perform(delete("/permission/permName")).
                andExpect(status().isNotFound()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$.error").exists());
    }

}
