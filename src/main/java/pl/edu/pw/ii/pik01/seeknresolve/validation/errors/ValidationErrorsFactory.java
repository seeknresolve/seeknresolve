package pl.edu.pw.ii.pik01.seeknresolve.validation.errors;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ValidationErrorsFactory {
    public ValidationErrors buildValidationErrors(BindingResult bindingResult) {
        ValidationErrors validationErrors = new ValidationErrors();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> validationErrors.addFieldError(fieldError));
        return validationErrors;
    }
}
