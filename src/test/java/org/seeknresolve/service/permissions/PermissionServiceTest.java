package org.seeknresolve.service.permissions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.seeknresolve.domain.dto.PermissionDTO;
import org.seeknresolve.domain.entity.Permission;
import org.seeknresolve.domain.repository.PermissionRepository;
import org.seeknresolve.domain.repository.RoleRepository;
import org.seeknresolve.service.common.DtosFactory;
import org.seeknresolve.service.permission.PermissionService;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RoleRepository roleRepository;

    private PermissionService permissionService;

    @Before
    public void setUp() {
        permissionService = new PermissionService(permissionRepository, roleRepository);
    }

    @Test
    public void shouldCreatePermissionFromDTO() {
        when(permissionRepository.save(any(Permission.class))).thenReturn(new Permission("add_all"));
        PermissionDTO arg = DtosFactory.createPermissionDTO(new Permission("add_all"));

        PermissionDTO saved = permissionService.createAndSaveNewPermission(arg);

        assertThat(saved).isNotNull();
        assertThat(saved.getPermissionName()).isEqualTo(arg.getPermissionName());
        verify(permissionRepository, times(1)).save(any(Permission.class));
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteFailWhenNoPermission() {
        when(permissionRepository.exists(anyString())).thenReturn(false);

        permissionService.delete("perm_name");
    }

    @Test
    public void delete() {
        when(permissionRepository.exists(anyString())).thenReturn(true);

        permissionService.delete("perm_name");

        verify(permissionRepository, times(1)).delete("perm_name");
    }
}
