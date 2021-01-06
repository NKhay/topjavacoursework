package ru.topjavacoursework.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;

import static ru.topjavacoursework.DishTestData.*;
import static ru.topjavacoursework.RestaurantTestData.REST1_ID;
import static ru.topjavacoursework.RestaurantTestData.REST2_ID;


public class DishServiceTest extends AbstractServiceTest {

    @Autowired
    private DishService service;

    @Test
    public void testGet() throws Exception {
        MATCHER.assertEquals(DISH1_REST1, service.get(DISH1_REST1_ID, REST1_ID));
    }

    @Test
    public void testGetNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(DISH1_REST1_ID, REST2_ID);
    }

    @Test
    public void testSave() throws Exception {
        Dish dish = getNew();
        Dish saved = service.save(dish, REST2_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(saved, DISH1_REST2, DISH2_REST2), service.getAll(REST2_ID, LocalDate.now()));
    }

    @Test
    public void testUpdate() throws Exception {
        Dish updated = getUpdated();
        service.update(updated, REST1_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DISH2_REST1, DISH1_REST1, updated), service.getAll(REST1_ID, LocalDate.now()));
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(DISH1_REST1_ID, REST1_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DISH2_REST1, DISH3_REST1), service.getAll(REST1_ID, LocalDate.now()));
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(DISH1_REST1_ID, REST2_ID);
    }

    @Test
    public void testGetAll() throws Exception {
        MATCHER.assertCollectionEquals(REST1_DISHES, service.getAll(REST1_ID, LocalDate.now()));
    }

}