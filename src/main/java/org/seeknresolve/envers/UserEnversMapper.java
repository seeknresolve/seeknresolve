package org.seeknresolve.envers;

import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.envers.fieldMapper.StringMapper;
import org.seeknresolve.envers.fieldMapper.UserRoleMapper;

public class UserEnversMapper extends EnversMapper<User>{

    public UserEnversMapper() {
        registerFieldMapper(String.class, new StringMapper());
        registerFieldMapper(UserRole.class, new UserRoleMapper());
    }
}
