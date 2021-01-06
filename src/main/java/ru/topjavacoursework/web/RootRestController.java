package ru.topjavacoursework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.service.UserService;
import ru.topjavacoursework.to.UserTo;
import ru.topjavacoursework.util.exception.ErrorInfo;
import ru.topjavacoursework.web.user.AbstractUserRestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

import static ru.topjavacoursework.util.ModelUtil.createFromTo;
import static ru.topjavacoursework.util.ValidationUtil.checkNew;


@RestController
public class RootRestController {
    private static final Logger LOG = LoggerFactory.getLogger(RootRestController.class);

    static final String REST_URL = "/api/register";

    private final UserService service;

    private GlobalControllerExceptionHandler exceptionInfoHandler;

    @Autowired
    public RootRestController(UserService service) {
        this.service = service;
    }

    @Autowired
    public void setExceptionInfoHandler(GlobalControllerExceptionHandler exceptionInfoHandler) {
        this.exceptionInfoHandler = exceptionInfoHandler;
    }

    @PostMapping(value = REST_URL)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        LOG.info("Register {}", userTo);
        checkNew(userTo);
        User created = service.save(createFromTo(userTo));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> duplicateEmailException(HttpServletRequest req, DataIntegrityViolationException e) {
        return exceptionInfoHandler.getErrorInfoResponseEntity(req, e, AbstractUserRestController.EXCEPTION_DUPLICATE_EMAIL, HttpStatus.CONFLICT);
    }

}
