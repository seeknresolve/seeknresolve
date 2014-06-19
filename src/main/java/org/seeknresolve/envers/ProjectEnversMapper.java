package org.seeknresolve.envers;

import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.envers.fieldMapper.StringMapper;

public class ProjectEnversMapper extends EnversMapper<Project> {

    public ProjectEnversMapper() {
        registerFieldMapper(String.class, new StringMapper());
    }
}
