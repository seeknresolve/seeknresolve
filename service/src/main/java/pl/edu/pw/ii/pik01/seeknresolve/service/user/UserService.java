package pl.edu.pw.ii.pik01.seeknresolve.service.user;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.security.ContextUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProjectRoleRepository userProjectRoleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserProjectRoleRepository userProjectRoleRepository) {
        this.userRepository = userRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
    }

    public User getLoggedUser() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // we have to get user with refreshed information
        return userRepository.findOneByLogin(principal.getUser().getLogin());
    }

    public UserDTO getLoggedUserDTO() {
        return DtosFactory.createUserDTO(getLoggedUser());
    }

    public UserDTO createAndSaveNewUser(UserDTO userDTO){
        User user = userRepository.save(crateUserFromUserDTO(userDTO));
        return user == null ? null : DtosFactory.createUserDTO(user);
    }

    public UserDTO findById(Long id){
        User user = userRepository.findOne(id);
        if(user == null){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
        return DtosFactory.createUserDTO(user);
    }

    public UserDTO findByLogin(String login){
        User user = userRepository.findOneByLogin(login);
        if(user == null){
            throw new EntityNotFoundException("User with login=" + login + " not found.");
        }
        return DtosFactory.createUserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return Lists.newArrayList(userRepository.findAll()).stream().
                map(user -> DtosFactory.createUserDTO(user)).collect(Collectors.toList());
    }

    private User crateUserFromUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setLogin(userDTO.getLogin());
        //TODO: should initialize lists and other?
        return user;
    }

    public List<UserDTO> getAllUserWithRolesOnProject(Long projectId) {
        List<UserProjectRole> projectRoles = userProjectRoleRepository.findByProjectId(projectId);
        return projectRoles.stream()
                .map(userProjectRole -> userProjectRole.getUser())
                .map(user -> DtosFactory.createUserDTO(user))
                .collect(Collectors.toList());
    }
}
