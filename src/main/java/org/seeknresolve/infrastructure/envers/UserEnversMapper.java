package org.seeknresolve.infrastructure.envers;

import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserRole;
import org.seeknresolve.infrastructure.envers.fieldMapper.StringMapper;
import org.seeknresolve.infrastructure.envers.fieldMapper.UserRoleMapper;

public class UserEnversMapper extends EnversMapper<User>{

    public UserEnversMapper() {
        registerFieldMapper(String.class, new StringMapper());
        registerFieldMapper(UserRole.class, new UserRoleMapper());
    }
}
