package org.seeknresolve.service.project;

import com.google.common.collect.Lists;
import org.seeknresolve.domain.dto.RevisionDiffDTO;
import org.seeknresolve.domain.entity.Project;
import org.seeknresolve.domain.repository.ProjectRepository;
import org.seeknresolve.infrastructure.envers.ProjectEnversMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.util.List;

@Service
public class ProjectRevisionService {

    private ProjectRepository projectRepository;
    private ProjectEnversMapper projectEnversMapper = new ProjectEnversMapper();

    @Autowired
    public ProjectRevisionService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<RevisionDiffDTO> getAllForProject(long projectID) throws IntrospectionException, IllegalAccessException {
        List<Revision<Integer, Project>> revisions = Lists.newArrayList(projectRepository.getAllRevisions(projectID));
        return projectEnversMapper.diff(revisions);
    }
}
