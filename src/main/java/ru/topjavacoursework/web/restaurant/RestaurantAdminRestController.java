package ru.topjavacoursework.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjavacoursework.model.Restaurant;
import ru.topjavacoursework.service.RestaurantService;
import ru.topjavacoursework.util.exception.ErrorInfo;
import ru.topjavacoursework.web.GlobalControllerExceptionHandler;
import ru.topjavacoursework.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(RestaurantAdminRestController.REST_URL)
public class RestaurantAdminRestController {
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantAdminRestController.class);

    static final String REST_URL = "/api/admin/restaurants";

    private static final String EXCEPTION_DUPLICATE_RESTAURANT = "Restaurant with this name already exists";

    private final RestaurantService service;
    private GlobalControllerExceptionHandler exceptionInfoHandler;

    @Autowired
    public RestaurantAdminRestController(RestaurantService service) {
        this.service = service;
    }

    @Autowired
    public void setExceptionInfoHandler(GlobalControllerExceptionHandler exceptionInfoHandler) {
        this.exceptionInfoHandler = exceptionInfoHandler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAll() {
        LOG.info("Get all restaurants");
        return service.getAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant get(@PathVariable("id") int id) {
        LOG.info("Get Restaurant {}", id);
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        LOG.info("Create {}", restaurant);
        ValidationUtil.checkNew(restaurant);
        Restaurant created = service.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable("id") int id) {
        LOG.info("Update {} with id = {}", restaurant, id);
        ValidationUtil.checkIdConsistent(restaurant, id);
        service.update(restaurant);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        LOG.info("Delete Restaurant {}", id);
        service.delete(id);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> duplicateEmailException(HttpServletRequest req, DataIntegrityViolationException e) {
        return exceptionInfoHandler.getErrorInfoResponseEntity(req, e, EXCEPTION_DUPLICATE_RESTAURANT, HttpStatus.CONFLICT);
    }

}
