package ru.topjavacoursework;

import ru.topjavacoursework.matcher.ModelMatcher;
import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.to.DishTo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ru.topjavacoursework.model.BaseEntity.START_SEQ;


public class DishTestData {
    public static final ModelMatcher<Dish> MATCHER = ModelMatcher.of(Dish.class);
    public static final ModelMatcher<DishTo> MATCHER_TO = ModelMatcher.of(DishTo.class);

    public static final int DISH1_REST1_ID = START_SEQ + 4;
    public static final int DISH1_REST2_ID = START_SEQ + 7;

    public static final Dish DISH1_REST1 = new Dish(DISH1_REST1_ID, "Fish", RestaurantTestData.REST1, LocalDate.now(), 100);
    public static final Dish DISH2_REST1 = new Dish(DISH1_REST1_ID + 1, "Chips", RestaurantTestData.REST1, LocalDate.now(), 50);
    public static final Dish DISH3_REST1 = new Dish(DISH1_REST1_ID + 2, "Still water", RestaurantTestData.REST1, LocalDate.now(), 15);
    public static final List<Dish> REST1_DISHES = Arrays.asList(DISH2_REST1, DISH1_REST1, DISH3_REST1);

    public static final Dish DISH1_REST2 = new Dish(DISH1_REST2_ID, "Soup", RestaurantTestData.REST2, LocalDate.now(), 100);
    public static final Dish DISH2_REST2 = new Dish(DISH1_REST2_ID + 1, "Steak", RestaurantTestData.REST2, LocalDate.now(), 250);
    public static final List<Dish> REST2_DISHES = Arrays.asList(DISH1_REST2, DISH2_REST2);

    public static Dish getNew() {
        return new Dish(null, "Juice", RestaurantTestData.REST2, LocalDate.now(), 50);
    }
    public static DishTo getNewTo() {
        return new DishTo(null, "Juice", 50);
    }
    public static Dish getUpdated() {
        return new Dish(DISH1_REST1_ID + 2, "Still water", RestaurantTestData.REST1, LocalDate.now(), 15);
    }
    public static DishTo getUpdatedTo() {
        return new DishTo(DISH1_REST1_ID + 2, "Still water", 15);
    }

}
