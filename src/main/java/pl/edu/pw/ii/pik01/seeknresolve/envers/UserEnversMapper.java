package pl.edu.pw.ii.pik01.seeknresolve.envers;

import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.UserRole;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.StringMapper;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.UserRoleMapper;

public class UserEnversMapper extends EnversMapper<User>{

    public UserEnversMapper() {
        registerFieldMapper(String.class, new StringMapper());
        registerFieldMapper(UserRole.class, new UserRoleMapper());
    }
}
