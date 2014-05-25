package pl.edu.pw.ii.pik01.seeknresolve.service.role;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.RoleDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.ProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Role;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    public List<RoleDTO> getAllUserRoles() {
        return getAllRolesOfType(UserRole.class);
    }

    public List<RoleDTO> getAllProjectRoles() {
        return getAllRolesOfType(ProjectRole.class);
    }

    private List<RoleDTO> getAllRolesOfType(Class<? extends Role> clazz) {
        return Lists.newArrayList(roleRepository.findAll())
                .stream()
                .filter(role -> role.getClass() == clazz)
                .map(userRole -> DtosFactory.createRoleDTO(userRole))
                .collect(Collectors.toList());
    }
}
