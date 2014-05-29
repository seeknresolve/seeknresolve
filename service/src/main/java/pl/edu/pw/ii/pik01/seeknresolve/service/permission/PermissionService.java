package pl.edu.pw.ii.pik01.seeknresolve.service.permission;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.PermissionDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Permission;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.PermissionRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<PermissionDTO> getAll() {
        return Lists.newArrayList(permissionRepository.findAll())
                .stream()
                .map(permission -> DtosFactory.createPermissionDTO(permission))
                .collect(Collectors.toList());
    }

    public PermissionDTO createAndSaveNewPermission(PermissionDTO permissionDTO) {
        Permission permission = createPermissionFromDTO(permissionDTO);
        Permission savedPermission = permissionRepository.save(permission);
        if(savedPermission == null) {
            throw new PersistenceException("Cannot save permission " + permission.getPermissionName());
        }
        return DtosFactory.createPermissionDTO(permission);
    }

    public void delete(String permissionName) {
        if(!permissionRepository.exists(permissionName)) {
            throw new EntityNotFoundException("Permission with name " + permissionName + " doesn't exist");
        }
        permissionRepository.delete(permissionName);
    }

    public PermissionDTO get(String permissionName) {
        if(!permissionRepository.exists(permissionName)) {
            throw new EntityNotFoundException("Permission with name " + permissionName + " doesn't exist");
        }
        return DtosFactory.createPermissionDTO(permissionRepository.findOne(permissionName));
    }

    private Permission createPermissionFromDTO(PermissionDTO permissionDTO) {
        return new Permission(permissionDTO.getPermissionName());
    }
}
