package ru.topjavacoursework.service;

import ru.topjavacoursework.model.Restaurant;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;


public interface RestaurantService {

    Restaurant get(int id) throws NotFoundException;

    Restaurant getWithDishes(int id, LocalDate menuDate) throws NotFoundException;

    Restaurant save(Restaurant restaurant);

    void update(Restaurant restaurant) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    List<Restaurant> getAll();

    List<Restaurant> getAllWithDishes(LocalDate menuDate);

    void evictCache();

}
