package ru.topjavacoursework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.dao.DishDao;
import ru.topjavacoursework.dao.RestaurantDao;
import ru.topjavacoursework.to.DishTo;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.topjavacoursework.util.ModelUtil.createFromTo;
import static ru.topjavacoursework.util.ModelUtil.updateFromTo;
import static ru.topjavacoursework.util.ValidationUtil.checkNotFoundWithId;


@Service
public class DishServiceImpl implements DishService {
    private final DishDao dao;
    private final RestaurantDao restaurantDao;

    @Autowired
    public DishServiceImpl(DishDao dao, RestaurantDao restaurantDao) {
        this.dao = dao;
        this.restaurantDao = restaurantDao;
    }

    @Override
    public Dish get(int id, int restaurantId) throws NotFoundException {
        Dish dish = dao.getOne(id);
        return checkNotFoundWithId(dish != null && dish.getRestaurant().getId() == restaurantId ? dish : null, id);
    }

    @Override
    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish can't be null");
        dish.setRestaurant(restaurantDao.getOne(restaurantId));
        return dao.save(dish);
    }

    @Override
    public Dish save(DishTo dishTo, int restaurantId) {
        return save(createFromTo(dishTo), restaurantId);
    }

    @Override
    @Transactional
    public void update(Dish dish, int restaurantId) throws NotFoundException {
        Assert.notNull(dish, "dish can't be null");
        get(dish.getId(), restaurantId);
        save(dish, restaurantId);
    }

    @Override
    @Transactional
    public void update(DishTo dishTo, int restaurantId) throws NotFoundException {
        Dish dish = updateFromTo(get(dishTo.getId(), restaurantId), dishTo);
        save(dish, restaurantId);
    }

    @Override
    public void delete(int id, int restaurantId) throws NotFoundException {
        checkNotFoundWithId(dao.delete(id, restaurantId) != 0, id);
    }

    @Override
    public List<Dish> getAll(int restaurantId, LocalDate date) {
        return dao.findAllByRestaurantIdAndDateOrderByName(restaurantId, date);
        //return dao.findAllByRestaurantIdOrderByName(restaurantId);
    }

}
