package pl.edu.pw.ii.pik01.seeknresolve.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;
import pl.edu.pw.ii.pik01.seeknresolve.service.security.ContextUser;

@Component("auditorAware")
class SpringSecurityAuditorAware implements AuditorAware<User> {
    @Override
    public User getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return ((ContextUser)authentication.getPrincipal()).getUser();
    }
}
