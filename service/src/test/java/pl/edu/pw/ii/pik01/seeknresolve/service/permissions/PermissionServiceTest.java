package pl.edu.pw.ii.pik01.seeknresolve.service.permissions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.PermissionDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Permission;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.PermissionRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    private PermissionService permissionService;

    @Before
    public void setUp() {
        permissionService = new PermissionService(permissionRepository);
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


}
