package pl.edu.pw.ii.pik01.seeknresolve.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import pl.edu.pw.ii.pik01.seeknresolve.domain.repository.ProjectRepository;
import pl.edu.pw.ii.pik01.seeknresolve.service.permission.PermissionChecker;
import pl.edu.pw.ii.pik01.seeknresolve.service.security.CustomPermissionEvaluator;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PermissionChecker permissionChecker;

    public MethodSecurityConfig() {
    }

    @Autowired
    public MethodSecurityConfig(ProjectRepository projectRepository, PermissionChecker permissionChecker) {
        this.projectRepository = projectRepository;
        this.permissionChecker = permissionChecker;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomPermissionEvaluator permissionEvaluator = new CustomPermissionEvaluator(projectRepository, permissionChecker);

        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}
