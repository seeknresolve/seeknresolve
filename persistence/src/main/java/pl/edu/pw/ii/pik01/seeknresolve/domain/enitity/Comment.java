package pl.edu.pw.ii.pik01.seeknresolve.domain.enitity;

import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private DateTime dateCreated;

    @ManyToOne(optional = false)
    private User author;

    @ManyToOne(optional = false)
    private Bug bug;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
