package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDTO {
    private Long id;
    private String content;
    private DateTime dateCreated;
    private Long authorId;
    private String authorLogin;
    private String bugTag;

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

    public String getAuthorLogin() {
        return authorLogin;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public String getBugTag() {
        return bugTag;
    }

    public void setBugTag(String bugTag) {
        this.bugTag = bugTag;
    }
}
