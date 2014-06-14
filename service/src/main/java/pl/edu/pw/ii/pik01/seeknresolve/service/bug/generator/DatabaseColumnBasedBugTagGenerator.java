package pl.edu.pw.ii.pik01.seeknresolve.service.bug.generator;

import org.springframework.stereotype.Component;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Project;

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
