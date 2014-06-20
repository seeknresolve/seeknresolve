package org.seeknresolve.infrastructure.audit;

import org.seeknresolve.domain.entity.User;
import org.seeknresolve.service.security.ContextUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auditorAware")
class SecurityUserAuditorAware implements AuditorAware<User> {
    @Override
    public User getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return ((ContextUser)authentication.getPrincipal()).getUser();
    }
}
