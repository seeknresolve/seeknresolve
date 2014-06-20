package org.seeknresolve.infrastructure.envers;

import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.infrastructure.envers.fieldMapper.StringMapper;

public class ProjectEnversMapper extends EnversMapper<Project> {

    public ProjectEnversMapper() {
        registerFieldMapper(String.class, new StringMapper());
    }
}
