package ru.topjavacoursework.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.service.UserService;
import ru.topjavacoursework.to.UserTo;
import ru.topjavacoursework.util.exception.ErrorInfo;
import ru.topjavacoursework.web.GlobalControllerExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ru.topjavacoursework.util.ValidationUtil.checkIdConsistent;
import static ru.topjavacoursework.util.ValidationUtil.checkNew;


public abstract class AbstractUserRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";

    private final UserService service;

    private GlobalControllerExceptionHandler exceptionInfoHandler;

    public AbstractUserRestController(UserService service) {
        this.service = service;
    }

    @Autowired
    public void setExceptionInfoHandler(GlobalControllerExceptionHandler exceptionInfoHandler) {
        this.exceptionInfoHandler = exceptionInfoHandler;
    }

    public List<User> getAll() {
        log.info("Get all users");
        return service.getAll();
    }

    public User get(int id) {
        log.info("Get User {}", id);
        return service.get(id);
    }

    public User create(User user) {
        log.info("Create {}", user);
        checkNew(user);
        return service.save(user);
    }

    public void delete(int id) {
        log.info("Delete User {}", id);
        service.delete(id);
    }

    public void update(User user, int id) {
        log.info("Update {} with id = {}", user, id);
        checkIdConsistent(user, id);
        service.update(user);
    }

    public void update(UserTo userTo, int id) {
        log.info("Update {} with id = {}", userTo, id);
        checkIdConsistent(userTo, id);
        service.update(userTo);
    }

    public User getByMail(String email) {
        log.info("Get User by email {}", email);
        return service.getByEmail(email);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> duplicateEmailException(HttpServletRequest req, DataIntegrityViolationException e) {
        return exceptionInfoHandler.getErrorInfoResponseEntity(req, e, EXCEPTION_DUPLICATE_EMAIL, HttpStatus.CONFLICT);
    }
}