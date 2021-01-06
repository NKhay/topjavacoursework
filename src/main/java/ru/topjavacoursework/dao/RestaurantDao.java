package ru.topjavacoursework.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjavacoursework.model.Restaurant;

import java.time.LocalDate;
import java.util.List;

//import static ModelUtil.SORT_BY_NAME;


@Repository
@Transactional(readOnly = true)
public interface RestaurantDao extends JpaRepository<Restaurant, Integer> {

    @Transactional
    @Override
    Restaurant save(Restaurant s);

    @Override
    Restaurant getOne(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    default List<Restaurant> findAll() {
        return findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE r.id=:id AND d.date=:date")
    Restaurant getWithDishes(@Param("id") int id, @Param("date") LocalDate menuDate);

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.dishes d WHERE d.date=:date ORDER BY r.name")
    List<Restaurant> getAllWithDishes(@Param("date") LocalDate menuDate);

}
