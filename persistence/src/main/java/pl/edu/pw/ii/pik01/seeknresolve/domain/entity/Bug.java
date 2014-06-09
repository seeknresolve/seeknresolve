package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import com.google.common.collect.ImmutableList;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Indexed
@Audited
//TODO: get rid of it by creating orm.xml (persistence.xml)
@EntityListeners({AuditingEntityListener.class})
public class Bug {
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Id
    @Column
    private String tag;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(nullable = false)
    private String name;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(nullable = false)
    private String description;

    @CreatedDate
    @Column(nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateModified;

    @ManyToOne(optional = false)
    private User reporter;

    @ManyToOne(optional = true)
    private User assignee;

    @ManyToOne(optional = false)
    private Project project;

    @NotAudited
    @OneToMany(mappedBy = "bug")
    private List<Attachment> attachments = new ArrayList<>();

    @NotAudited
    @OneToMany(mappedBy = "bug")
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    public enum State {
        NEW,
        REJECTED,
        IN_PROGRESS,
        READY_TO_TEST,
        REOPENED,
        STOPPED,
        CLOSED
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        CRITICAL
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public DateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Attachment> getAttachments() {
        return ImmutableList.copyOf(attachments);
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }

    public List<Comment> getComments() {
        return ImmutableList.copyOf(comments);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
