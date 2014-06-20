package org.seeknresolve.infrastructure.envers;

import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.infrastructure.envers.fieldMapper.PriorityMapper;
import org.seeknresolve.infrastructure.envers.fieldMapper.StateMapper;
import org.seeknresolve.infrastructure.envers.fieldMapper.StringMapper;
import org.seeknresolve.infrastructure.envers.fieldMapper.UserMapper;

public class BugEnversMapper extends EnversMapper<Bug> {

    public BugEnversMapper() {
        registerFieldMapper(User.class, new UserMapper());
        registerFieldMapper(String.class, new StringMapper());
        registerFieldMapper(Bug.Priority.class, new PriorityMapper());
        registerFieldMapper(Bug.State.class, new StateMapper());
    }

}
