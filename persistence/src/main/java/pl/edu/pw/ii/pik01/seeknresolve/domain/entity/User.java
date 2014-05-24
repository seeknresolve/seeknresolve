package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import com.google.common.collect.ImmutableList;

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

    private boolean enabled;

    private boolean expired;

    private boolean locked;

    @OneToMany(mappedBy = "reporter")
    private List<Bug> bugsReported = new ArrayList<>();

    @OneToMany(mappedBy = "assignee")
    private List<Bug> bugsAssigned = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();
    
    @ManyToOne(targetEntity = UserRole.class, optional = false)
    @JoinColumn(name = "user_role")
    private UserRole userRole;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Bug> getReportedBugs() {
        return ImmutableList.copyOf(bugsReported);
    }

    public void addReportedBug(Bug bug) {
        bugsReported.add(bug);
    }

    public List<Bug> getAssignedBugs() {
        return ImmutableList.copyOf(bugsAssigned);
    }

    public void addAssignedBug(Bug bug) {
        bugsAssigned.add(bug);
    }

    public List<Comment> getComments() {
        return ImmutableList.copyOf(comments);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
