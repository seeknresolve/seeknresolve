package pl.edu.pw.ii.pik01.seeknresolve.service.user;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pw.ii.pik01.seeknresolve.domain.dto.UserDTO;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.UserRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.common.DtosFactory;
import pl.edu.pw.ii.pik01.seeknresolve.service.security.ContextUser;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DtosFactory dtosFactory;

    @Autowired
    public UserService(UserRepository userRepository, DtosFactory dtosFactory) {
        this.userRepository = userRepository;
        this.dtosFactory = dtosFactory;
    }

    public User getLoggedUser() {
        ContextUser principal = (ContextUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // we have to get user with refreshed information
        return userRepository.findOneByLogin(principal.getUser().getLogin());
    }

    public UserDTO getLoggedUserDTO() {
        return dtosFactory.createUserDTO(getLoggedUser());
    }

    public UserDTO createAndSaveNewUser(UserDTO userDTO){
        User user = userRepository.save(crateUserFromUserDTO(userDTO));
        return user == null ? null : dtosFactory.createUserDTO(user);
    }

    public UserDTO findOneById(Long id){
        User user = userRepository.findOne(id);
        if(user == null){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
        return dtosFactory.createUserDTO(user);
    }

    public UserDTO findOneByLogin(String login){
        User user = userRepository.findOneByLogin(login);
        if(user == null){
            throw new EntityNotFoundException("User with login=" + login + " not found.");
        }
        return dtosFactory.createUserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return Lists.newArrayList(userRepository.findAll()).stream().
                map(user -> dtosFactory.createUserDTO(user)).collect(Collectors.toList());
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
}
