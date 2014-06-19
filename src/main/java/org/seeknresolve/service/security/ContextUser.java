package org.seeknresolve.service.security;

import com.google.common.collect.ImmutableSet;
import org.seeknresolve.domain.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class ContextUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public ContextUser(User user) {
        super(user.getLogin(),
                user.getPassword(),
                user.isEnabled(),
                !user.isExpired(),
                true,
                !user.isLocked(),
                ImmutableSet.of(new SimpleGrantedAuthority(user.getUserRole().getRoleName())));

        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
