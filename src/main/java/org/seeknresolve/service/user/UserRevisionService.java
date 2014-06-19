package org.seeknresolve.service.user;

import com.google.common.collect.Lists;
import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.repository.UserRepository;
import org.seeknresolve.envers.UserEnversMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.beans.IntrospectionException;
import java.util.List;

@Service
public class UserRevisionService {

    private final UserRepository userRepository;
    private final UserEnversMapper userEnversMapper = new UserEnversMapper();

    @Autowired
    public UserRevisionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<RevisionDiffDTO> getAllForUser(String login) throws IntrospectionException, IllegalAccessException {
        User user = userRepository.findOneByLogin(login);
        if(user == null) {
            throw  new EntityNotFoundException("User not found");
        }
        List<Revision<Integer, User>> revisions = Lists.newArrayList(userRepository.getAllRevisions(user.getId()));
        return userEnversMapper.diff(revisions);
    }
}
