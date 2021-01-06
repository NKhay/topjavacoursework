package ru.topjavacoursework.service;

import ru.topjavacoursework.model.User;
import ru.topjavacoursework.to.UserTo;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.util.List;


public interface UserService {

    User get(int id) throws NotFoundException;

    User save(User user);

    void update(User user) throws NotFoundException;

    void update(UserTo userTo) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    List<User> getAll();

    User getByEmail(String email) throws NotFoundException;

}
