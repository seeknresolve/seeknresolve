package pl.edu.pw.ii.pik01.seeknresolve.service.user;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.CreateUserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserProjectRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.RoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserProjectRoleRepository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.security.ContextUser;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProjectRoleRepository userProjectRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, UserProjectRoleRepository userProjectRoleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userProjectRoleRepository = userProjectRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User getLoggedUser() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // we have to get user with refreshed information
        return userRepository.findOneByLogin(principal.getUser().getLogin());
    }

    @Transactional
    public UserDTO getLoggedUserDTO() {
        return DtosFactory.createUserDTO(getLoggedUser());
    }

    @Transactional
    public UserDTO createAndSaveNewUser(CreateUserDTO userDTO){
        User user = userRepository.save(crateUserFromUserDTO(userDTO));
        return user == null ? null : DtosFactory.createUserDTO(user);
    }

    @Transactional
    public UserDTO findById(Long id){
        User user = userRepository.findOne(id);
        if(user == null){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
        return DtosFactory.createUserDTO(user);
    }

    @Transactional
    public UserDTO findByLogin(String login){
        User user = userRepository.findOneByLogin(login);
        if(user == null){
            throw new EntityNotFoundException("User with login=" + login + " not found.");
        }
        return DtosFactory.createUserDTO(user);
    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        return Lists.newArrayList(userRepository.findAll()).stream().
                map(user -> DtosFactory.createUserDTO(user)).collect(Collectors.toList());
    }

    private User crateUserFromUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        if(userDTO instanceof CreateUserDTO) {
            user.setPassword(passwordEncoder.encode(((CreateUserDTO)userDTO).getPassword()));
        }
        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUserRole((UserRole) roleRepository.findOne(userDTO.getUserRole()));
        return user;
    }

    @Transactional
    public List<UserDTO> getAllUserWithRolesOnProject(Long projectId) {
        List<UserProjectRole> projectRoles = userProjectRoleRepository.findByProjectId(projectId);
        return projectRoles.stream()
                .map(userProjectRole -> userProjectRole.getUser())
                .map(user -> DtosFactory.createUserDTO(user))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findOneByLogin(userDTO.getLogin());
        updateUser(user, userDTO);
        User updatedUser = userRepository.save(user);
        if(updatedUser == null) {
            throw new PersistenceException("Cannot update user " + userDTO.getLogin());
        }
        return DtosFactory.createUserDTO(updatedUser);
    }

    private void updateUser(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setUserRole((UserRole) roleRepository.findOne(userDTO.getUserRole()));
    }
}
