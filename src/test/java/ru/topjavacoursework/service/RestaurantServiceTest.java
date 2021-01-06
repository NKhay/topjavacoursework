package ru.topjavacoursework.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjavacoursework.DishTestData;
import ru.topjavacoursework.JpaUtil;
import ru.topjavacoursework.model.Restaurant;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static ru.topjavacoursework.DishTestData.REST1_DISHES;
import static ru.topjavacoursework.RestaurantTestData.*;


public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService service;

    @Autowired
    private JpaUtil jpaUtil;

    @Before
    public void setUp() throws Exception {
        service.evictCache();
        jpaUtil.clear2ndLevelHibernateCache(Restaurant.class);
    }

    @Test
    public void testGet() throws Exception {
        Restaurant restaurant = service.get(REST1_ID);
        MATCHER.assertEquals(REST1, restaurant);
    }

    @Test
    public void testGetNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(1);
    }

    @Test
    public void testGetWithDishes() throws Exception {
        Restaurant restaurant = service.getWithDishes(REST1_ID, LocalDate.now());
        MATCHER.assertEquals(REST1, restaurant);
        DishTestData.MATCHER.assertCollectionEquals(restaurant.getDishes(), REST1_DISHES);
    }

    @Test
    public void testSave() throws Exception {
        Restaurant newRestaurant = getNew();
        Restaurant restaurant = service.save(newRestaurant);
        newRestaurant.setId(restaurant.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(newRestaurant, REST1, REST2), service.getAll());
    }

    @Test
    public void testUpdate() throws Exception {
        Restaurant updated = getUpdated();
        service.update(updated);
        MATCHER.assertEquals(updated, service.get(REST1_ID));
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(REST1_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(REST2), service.getAll());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1);
    }

    @Test
    public void testGetAll() throws Exception {
        MATCHER.assertCollectionEquals(RESTAURANTS, service.getAll());
    }

    @Test
    public void testGetAllWithDishes() throws Exception {
        MATCHER.assertCollectionEquals(RESTAURANTS, service.getAllWithDishes(LocalDate.now()));
    }

}