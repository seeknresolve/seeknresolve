package org.seeknresolve.validation.errors;

import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationErrors implements Serializable {
    private List<FieldError> fieldErrors = new ArrayList<>();

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(FieldError fieldError) {
        fieldErrors.add(fieldError);
    }
}
