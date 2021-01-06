package ru.topjavacoursework.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.service.DishService;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = DishRestController.REST_URL)
public class DishRestController {
    private static final Logger LOG = LoggerFactory.getLogger(DishRestController.class);

    static final String REST_URL = "/api/restaurants/{restaurantId}/dishes";

    private DishService service;

    @Autowired
    public DishRestController(DishService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> getAll(@PathVariable("restaurantId") int restaurantId) {
        LOG.info("Get all today dishes for Restaurant {}", restaurantId);
        return service.getAll(restaurantId, LocalDate.now());
    }

}
