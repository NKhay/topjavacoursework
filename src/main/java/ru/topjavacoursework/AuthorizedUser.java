package ru.topjavacoursework;

import ru.topjavacoursework.model.User;
import ru.topjavacoursework.to.UserTo;

import static ru.topjavacoursework.util.ModelUtil.asTo;


public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        userTo = asTo(user);
    }

    public int getId() {
        return userTo.getId();
    }

    @Override
    public String toString() {
        return userTo.toString();
    }

}
