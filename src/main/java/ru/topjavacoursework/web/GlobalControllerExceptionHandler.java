package ru.topjavacoursework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.topjavacoursework.util.ValidationUtil;
import ru.topjavacoursework.util.exception.ErrorInfo;
import ru.topjavacoursework.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ErrorInfo httpMethodNotSupportedException(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo restValidationError(HttpServletRequest req, MethodArgumentNotValidException e) {
        return logAndGetValidationErrorInfo(req, e.getBindingResult());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorInfo> handleNoHandlerFoundException(HttpServletRequest req, NoHandlerFoundException e) {
        return getErrorInfoResponseEntity(req, e, "Requested resource not found", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    private ErrorInfo logAndGetValidationErrorInfo(HttpServletRequest req, BindingResult result) {
        String[] details = result.getAllErrors().stream()
                .map(ValidationUtil::getMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, "ValidationException", details);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            LOG.error("Exception at request {}" + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("Exception at request {}: {}", req.getRequestURL() + ": " + rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), rootCause);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, String cause, String... details) {
        LOG.warn("{} exception at request {}: {}", cause, req.getRequestURL(), Arrays.toString(details));
        return new ErrorInfo(req.getRequestURL(), cause, details);
    }

    public ResponseEntity<ErrorInfo> getErrorInfoResponseEntity(HttpServletRequest req, Exception e, String message, HttpStatus httpStatus) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        LOG.warn("Application error: {}", rootCause.toString());
        ErrorInfo errorInfo = logAndGetErrorInfo(req, rootCause.getClass().getSimpleName(), message);
        return new ResponseEntity<>(errorInfo, httpStatus);
    }

}
