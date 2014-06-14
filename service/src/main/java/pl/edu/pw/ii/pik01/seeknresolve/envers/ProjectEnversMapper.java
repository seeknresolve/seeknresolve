package pl.edu.pw.ii.pik01.seeknresolve.envers;

import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;
import pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper.StringMapper;

public class ProjectEnversMapper extends EnversMapper<Project> {

    public ProjectEnversMapper() {
        registerFieldMapper(String.class, new StringMapper());
    }
}
