package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

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
