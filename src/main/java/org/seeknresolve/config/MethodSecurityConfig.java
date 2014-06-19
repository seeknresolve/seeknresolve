package org.seeknresolve.config;

import org.seeknresolve.domain.repository.ProjectRepository;
import org.seeknresolve.service.permission.PermissionChecker;
import org.seeknresolve.service.security.CustomPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

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
