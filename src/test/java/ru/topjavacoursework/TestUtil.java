package ru.topjavacoursework;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.topjavacoursework.model.User;


public class TestUtil {

    private TestUtil() {}

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

}
