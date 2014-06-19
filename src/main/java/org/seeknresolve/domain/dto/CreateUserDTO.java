package org.seeknresolve.domain.dto;

public class CreateUserDTO extends UserDTO{
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + getId() +
                ", login='" + getLogin() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", userRole='" + getUserRole() + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
