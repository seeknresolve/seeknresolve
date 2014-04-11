package pl.edu.pw.ii.pik01.seeknresolve.domain.enitity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "reporter")
    private List<Bug> bugsReported = new ArrayList<>();

    @OneToMany(mappedBy = "assigned")
    private List<Bug> bugsAssigned = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Bug> getReportedBugs() {
        return bugsReported;
    }

    public void addReportedBug(Bug bug) {
        bugsReported.add(bug);
    }

    public List<Bug> getAssignedBugs() {
        return bugsAssigned;
    }

    public void addAssignedBug(Bug bug) {
        bugsAssigned.add(bug);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
