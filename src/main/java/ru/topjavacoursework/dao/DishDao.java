package ru.topjavacoursework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjavacoursework.model.Dish;

import java.time.LocalDate;
import java.util.List;


@Repository
@Transactional(readOnly = true)
public interface DishDao extends JpaRepository<Dish, Integer> {

    @Override
    @Transactional
    Dish save(Dish s);

    @Override
    Dish getOne/*findOne*/(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    int delete(@Param("id") int id, @Param("restaurantId") int restaurantId);

    List<Dish> findAllByRestaurantIdAndDateOrderByName(int restaurantId, LocalDate date);

}
