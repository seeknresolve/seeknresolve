package pl.edu.pw.ii.pik01.seeknresolve.service.role;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RoleDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Permission;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.ProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Role;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.PermissionRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }
    
    public List<RoleDTO> getAllUserRoles() {
        return getAllRolesOfType(UserRole.class);
    }

    public List<RoleDTO> getAllProjectRoles() {
        return getAllRolesOfType(ProjectRole.class);
    }

    public RoleDTO getRole(String roleName) {
        Role role = roleRepository.findOne(roleName);
        if (role == null) {
           throw roleNotFound(roleName);
        }
        return DtosFactory.createRoleDTO(role);
    }

    public void addPermissionToRole(String roleName, String permissionName) {
        Role role = roleRepository.findOne(roleName);
        Permission permission = permissionRepository.findOne(permissionName);

        if(role == null || permission == null) {
            throw new EntityNotFoundException("");
        }

        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    public void deletePermissionFromRole(String roleName, String permissionName) {
        Role role = roleRepository.findOne(roleName);
        Permission permission = permissionRepository.findOne(permissionName);

        if(role == null || permission == null) {
            throw new EntityNotFoundException("");
        }

        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    private List<RoleDTO> getAllRolesOfType(Class<? extends Role> clazz) {
        return Lists.newArrayList(roleRepository.findAll())
                .stream()
                .filter(role -> role.getClass().equals(clazz))
                .map(userRole -> DtosFactory.createRoleDTO(userRole))
                .collect(Collectors.toList());
    }

    private EntityNotFoundException roleNotFound(String roleName) {
        return new EntityNotFoundException("Role with role name: " + roleName + " doesn't exists.");
    }
}
