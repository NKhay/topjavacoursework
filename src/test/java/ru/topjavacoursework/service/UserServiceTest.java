package ru.topjavacoursework.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.util.Arrays;

import static ru.topjavacoursework.UserTestData.*;


public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    public void get() throws Exception {
        User user = service.get(USER_ID);
        MATCHER.assertEquals(USER, user);
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(1);
    }

    @Test
    public void getByEmail() throws Exception {
        User user = service.getByEmail("anna@mail.com");
        MATCHER.assertEquals(USER, user);
    }

    @Test
    public void getByEmailNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.getByEmail("nouser@mail.com");
    }

    @Test
    public void save() throws Exception {
        User newUser = getNew();
        User user = service.save(newUser);
        newUser.setId(user.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, newUser, USER), service.getAll());
    }

    @Test
    public void update() throws Exception {
        User updated = getUpdated();
        service.update(updated);
        MATCHER.assertEquals(updated, service.get(USER_ID));
    }

    @Test
    public void delete() throws Exception {
        service.delete(USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN), service.getAll());
    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1);
    }

    @Test
    public void getAll() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, USER), service.getAll());
    }

}