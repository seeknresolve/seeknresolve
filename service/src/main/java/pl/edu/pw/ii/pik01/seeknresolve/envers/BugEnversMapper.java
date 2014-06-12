package pl.edu.pw.ii.pik01.seeknresolve.envers;

import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.PriorityMapper;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.StateMapper;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.StringMapper;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.UserMapper;

public class BugEnversMapper extends EnversMapper<Bug> {

    public BugEnversMapper() {
        registerFieldMapper(User.class, new UserMapper());
        registerFieldMapper(String.class, new StringMapper());
        registerFieldMapper(Bug.Priority.class, new PriorityMapper());
        registerFieldMapper(Bug.State.class, new StateMapper());
    }

}
