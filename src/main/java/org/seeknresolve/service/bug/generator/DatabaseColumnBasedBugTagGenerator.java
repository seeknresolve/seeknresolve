package org.seeknresolve.service.bug.generator;

import org.seeknresolve.domain.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class DatabaseColumnBasedBugTagGenerator implements BugTagGenerator {
    @Override
    public String generateTag(Project project) {
        String projectTag = project.getTag();
        long lastBugNumber = getNextBugNumber(project);
        return String.join("-", projectTag, String.valueOf(lastBugNumber));
    }

    private Long getNextBugNumber(Project project) {
        return project.getLastBugNumber() + 1;
    }
}
