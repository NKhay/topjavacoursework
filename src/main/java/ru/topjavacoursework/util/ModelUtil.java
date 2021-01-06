package ru.topjavacoursework.util;

import org.springframework.util.StringUtils;
import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.model.Role;
import ru.topjavacoursework.model.User;
import ru.topjavacoursework.to.DishTo;
import ru.topjavacoursework.to.UserTo;


public class ModelUtil {

    private ModelUtil() {
    }

    //public static final Sort SORT_BY_NAME = new Sort("name");
    //public static final Sort SORT_BY_NAME_EMAIL = new Sort("name", "email");

    public static Dish createFromTo(DishTo dishTo) {
        return new Dish(null, dishTo.getName(), null, dishTo.getPrice());
    }

    public static Dish updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
        dish.setPrice(dishTo.getPrice());
        return dish;
    }

    public static DishTo asTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    public static User createFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        if (!StringUtils.isEmpty(user.getEmail())) {
            user.setEmail(user.getEmail().toLowerCase());
        }
        return user;
    }

}
