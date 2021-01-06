package ru.topjavacoursework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.topjavacoursework.matcher.ModelMatcher;
import ru.topjavacoursework.model.Role;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.to.UserTo;
import ru.topjavacoursework.util.PasswordUtil;

import java.util.Objects;

import static ru.topjavacoursework.model.BaseEntity.START_SEQ;


public class UserTestData {
    private static final Logger LOG = LoggerFactory.getLogger(UserTestData.class);

    public static final int ADMIN_ID = START_SEQ;
    public static final int USER_ID = START_SEQ + 1;

    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@mail.com", "admin", Role.ADMIN, Role.USER);
    public static final User USER = new User(USER_ID, "Anna", "anna@mail.com", "anna", Role.USER);

    public static final ModelMatcher<User> MATCHER = ModelMatcher.of(User.class,
            (expected, actual) -> expected == actual ||
                    (comparePassword(expected.getPassword(), actual.getPassword())
                            && Objects.equals(expected.getId(), actual.getId())
                            && Objects.equals(expected.getName(), actual.getName())
                            && Objects.equals(expected.getEmail(), actual.getEmail())
                            && Objects.equals(expected.isEnabled(), actual.isEnabled())
                            && Objects.equals(expected.getRoles(), actual.getRoles())
                    )
    );

    public static User getNew() {
        return new User(null, "New User", "newuser@mail.com", "newuser", Role.USER);
    }
    public static User getUpdated() {
        return new User(START_SEQ, "Updated User", "user@mail.com", "user", Role.USER);
    }
    public static UserTo getUpdatedTo() {
        return new UserTo(START_SEQ, "Updated User", "updateduser@mail.com", "user");
    }

    private static boolean comparePassword(String rawOrEncodedPassword, String password) {
        if (PasswordUtil.isEncoded(rawOrEncodedPassword)) {
            return rawOrEncodedPassword.equals(password);
        } else if (!PasswordUtil.isMatch(rawOrEncodedPassword, password)) {
            LOG.error("Password " + password + " doesn't match encoded " + password);
            return false;
        }
        return true;
    }

}
