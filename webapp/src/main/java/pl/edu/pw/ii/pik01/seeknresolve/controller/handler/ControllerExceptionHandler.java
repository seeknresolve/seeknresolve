package pl.edu.pw.ii.pik01.seeknresolve.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.edu.pw.ii.pik01.seeknresolve.service.exception.EntityNotFoundException;
import pl.edu.pw.ii.pik01.seeknresolve.service.response.ErrorResponse;
import pl.edu.pw.ii.pik01.seeknresolve.validation.errors.ValidationErrors;
import pl.edu.pw.ii.pik01.seeknresolve.validation.errors.ValidationErrorsFactory;

import javax.persistence.PersistenceException;

@ControllerAdvice(annotations = RestController.class)
public class ControllerExceptionHandler {
    private Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private ValidationErrorsFactory validationErrorsFactory;

    public ControllerExceptionHandler() {
        this.validationErrorsFactory = new ValidationErrorsFactory();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFound(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handlePersistenceErrors(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleSecurityException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrors handleException(MethodArgumentNotValidException exception) {
        return validationErrorsFactory.buildValidationErrors(exception.getBindingResult());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handle(HttpMessageNotReadableException exception) {
        log.error("BAD_REQUEST", exception);
        return new ErrorResponse(exception.getMessage());
    }
}
