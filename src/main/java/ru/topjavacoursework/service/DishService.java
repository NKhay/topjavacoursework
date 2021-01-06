package ru.topjavacoursework.service;

import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.to.DishTo;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;


public interface DishService {

    Dish get(int id, int restaurantId) throws NotFoundException;

    Dish save(Dish dish, int restaurantId);

    Dish save(DishTo dishTo, int restaurantId);

    void update(Dish dish, int restaurantId) throws NotFoundException;

    void update(DishTo dishTo, int restaurantId) throws NotFoundException;

    void delete(int id, int restaurantId) throws NotFoundException;

    List<Dish> getAll(int restaurantId, LocalDate date);

}
