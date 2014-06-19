package org.seeknresolve.service.user;

import com.google.common.collect.Lists;
import org.seeknresolve.domain.dto.ChangePasswordDTO;
import org.seeknresolve.domain.dto.CreateUserDTO;
import org.seeknresolve.domain.dto.UserDTO;
import org.seeknresolve.domain.dto.UserDetailsDTO;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserProjectRole;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.domain.repository.RoleRepository;
import org.seeknresolve.domain.repository.UserProjectRoleRepository;
import org.seeknresolve.domain.repository.UserRepository;
import org.seeknresolve.service.common.DtosFactory;
import org.seeknresolve.service.security.ContextUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

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
    public UserDetailsDTO findById(Long id){
        User user = userRepository.findOne(id);
        if(user == null){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
        return DtosFactory.createUserDetailsDTO(user);
    }

    @Transactional
    public UserDetailsDTO findByLogin(String login){
        User user = userRepository.findOneByLogin(login);
        if(user == null){
            throw new EntityNotFoundException("User with login=" + login + " not found.");
        }
        return DtosFactory.createUserDetailsDTO(user);
    }

    @Transactional
    public List<UserDTO> findNotAssignedToProject(Long projectId) {
        List<User> userList= userRepository.findNotAssignedToProject(projectId);
        return DtosFactory.createUserDTOList(userList);
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

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        checkArgument(changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmPassword()), "Passwords aren't equal");

        User user = userRepository.findOneByLogin(changePasswordDTO.getLogin());
        if(user == null) {
            throw new EntityNotFoundException("User with login=" + changePasswordDTO.getLogin() + " not found.");
        }

        String encodedPassword = passwordEncoder.encode(changePasswordDTO.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    private void updateUser(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setUserRole((UserRole) roleRepository.findOne(userDTO.getUserRole()));
    }

}
