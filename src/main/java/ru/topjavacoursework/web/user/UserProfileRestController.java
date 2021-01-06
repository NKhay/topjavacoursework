package ru.topjavacoursework.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.topjavacoursework.AuthorizedUser;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.service.UserService;
import ru.topjavacoursework.to.UserTo;

import javax.validation.Valid;


@RestController
@RequestMapping(UserProfileRestController.REST_URL)
public class UserProfileRestController extends AbstractUserRestController {
    public static final String REST_URL = "/api/profile";

    @Autowired
    public UserProfileRestController(UserService service) {
        super(service);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        return super.get(authorizedUser.getId());
    }

    @DeleteMapping
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        super.delete(authorizedUser.getId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody UserTo userTo, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        super.update(userTo, authorizedUser.getId());
    }

}