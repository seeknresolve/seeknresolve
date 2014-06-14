package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

public class ChangePasswordDTO {
    private String login;
    private String password;

    public ChangePasswordDTO() {
    }

    public ChangePasswordDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
