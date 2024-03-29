package org.seeknresolve.service.role;

import com.google.common.collect.Lists;
import org.seeknresolve.domain.dto.RoleDTO;
import org.seeknresolve.domain.entity.Permission;
import org.seeknresolve.domain.entity.ProjectRole;
import org.seeknresolve.domain.entity.Role;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.domain.repository.PermissionRepository;
import org.seeknresolve.domain.repository.RoleRepository;
import org.seeknresolve.service.common.DtosFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Transactional
    public List<RoleDTO> getAllUserRoles() {
        return getAllRolesOfType(UserRole.class);
    }

    @Transactional
    public List<RoleDTO> getAllProjectRoles() {
        return getAllRolesOfType(ProjectRole.class);
    }

    @Transactional
    public RoleDTO getRole(String roleName) {
        Role role = roleRepository.findOne(roleName);
        if (role == null) {
           throw roleNotFound(roleName);
        }
        return DtosFactory.createRoleDTO(role);
    }

    @Transactional
    public void addPermissionToRole(String roleName, String permissionName) {
        Role role = roleRepository.findOne(roleName);
        Permission permission = permissionRepository.findOne(permissionName);

        if(role == null || permission == null) {
            throw new EntityNotFoundException("Permission " + permissionName + " does not exist.");
        }

        role.getPermissions().add(permission);
        roleRepository.save(role);
    }

    @Transactional
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
