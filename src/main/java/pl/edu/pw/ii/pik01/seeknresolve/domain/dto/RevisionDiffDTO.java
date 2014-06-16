package pl.edu.pw.ii.pik01.seeknresolve.domain.dto;

import org.joda.time.DateTime;

public class RevisionDiffDTO {

    private DateTime modificationTime;
    private String description;

    public RevisionDiffDTO(DateTime modificationTime, String description) {
        this.modificationTime = modificationTime;
        this.description = description;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(DateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
