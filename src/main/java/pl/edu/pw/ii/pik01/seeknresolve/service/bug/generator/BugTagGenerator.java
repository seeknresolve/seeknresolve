package pl.edu.pw.ii.pik01.seeknresolve.service.bug.generator;

import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;

public interface BugTagGenerator {
    String generateTag(Project project);
}
