package org.seeknresolve.domain.test.builders;

import org.seeknresolve.domain.entity.User;
import org.seeknresolve.domain.entity.UserRole;

public class UserBuilder {

    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;

    public User build() {
        User user = new User();
        user.setLogin(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUserRole(userRole);
        return user;
    }

    public UserBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public UserBuilder withRole(UserRole userRole) {
        this.userRole = userRole;
        return this;
    }
}
