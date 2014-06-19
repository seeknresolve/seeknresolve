package org.seeknresolve.service.bug.generator;

import org.seeknresolve.domain.entity.Project;

public interface BugTagGenerator {
    String generateTag(Project project);
}
