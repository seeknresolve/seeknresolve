package pl.edu.pw.ii.pik01.seeknresolve.domain.enitity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "assignee")
    private List<Bug> bugsAssigned = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany()
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name="role_name", referencedColumnName = "roleName"))
    private Set<Role> roles = new HashSet<>();
    
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

    public Set<Role> getRoles() {
        return ImmutableSet.copyOf(roles);
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
