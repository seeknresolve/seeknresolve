package pl.edu.pw.ii.pik01.seeknresolve.service.user;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.enitity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getLoggedUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findOneByLogin(principal.getUsername());
    }

    public UserDTO createAndSaveNewUser(UserDTO userDTO){
        User user = userRepository.save(crateUserFromUserDTO(userDTO));
        return user == null ? null : buildDTOfromUser(user);
    }

    public UserDTO findOneById(Long id){
        User user = userRepository.findOne(id);
        if(user == null){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
        return buildDTOfromUser(user);
    }

    public UserDTO findOneByLogin(String login){
        User user = userRepository.findOneByLogin(login);
        if(user == null){
            throw new EntityNotFoundException("User with login=" + login + " not found.");
        }
        return buildDTOfromUser(user);
    }

    public List<UserDTO> getAllUsers() {
        return Lists.newArrayList(userRepository.findAll()).stream().
                map(user -> buildDTOfromUser(user)).collect(Collectors.toList());
    }

    private User crateUserFromUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setLogin(userDTO.getLogin());
        //TODO: should initialize lists and other?
        return user;
    }

    private UserDTO buildDTOfromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setUserRole(user.getUserRole().getRoleName());
        return userDTO;
    }
}
