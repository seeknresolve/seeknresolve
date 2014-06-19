package org.seeknresolve.envers;

import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.entity.User;
import org.seeknresolve.envers.fieldMapper.PriorityMapper;
import org.seeknresolve.envers.fieldMapper.StateMapper;
import org.seeknresolve.envers.fieldMapper.StringMapper;
import org.seeknresolve.envers.fieldMapper.UserMapper;

public class BugEnversMapper extends EnversMapper<Bug> {

    public BugEnversMapper() {
        registerFieldMapper(User.class, new UserMapper());
        registerFieldMapper(String.class, new StringMapper());
        registerFieldMapper(Bug.Priority.class, new PriorityMapper());
        registerFieldMapper(Bug.State.class, new StateMapper());
    }

}
