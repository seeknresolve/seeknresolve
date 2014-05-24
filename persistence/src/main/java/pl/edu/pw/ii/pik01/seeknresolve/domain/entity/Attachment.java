package pl.edu.pw.ii.pik01.seeknresolve.domain.entity;

import javax.persistence.*;

@Entity
public class Attachment {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private byte[] file; //TODO: what type it should be then?

    @ManyToOne(optional = false)
    private Bug bug;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
