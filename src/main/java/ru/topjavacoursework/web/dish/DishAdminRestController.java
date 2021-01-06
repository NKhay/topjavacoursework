package ru.topjavacoursework.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjavacoursework.model.Dish;
import ru.topjavacoursework.service.DishService;
import ru.topjavacoursework.to.DishTo;
import ru.topjavacoursework.util.exception.ErrorInfo;
import ru.topjavacoursework.web.GlobalControllerExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.topjavacoursework.util.ValidationUtil.checkIdConsistent;
import static ru.topjavacoursework.util.ValidationUtil.checkNew;


@RestController
@RequestMapping(value = DishAdminRestController.REST_URL)
public class DishAdminRestController {
    private static final Logger LOG = LoggerFactory.getLogger(DishAdminRestController.class);

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    private static final String EXCEPTION_DUPLICATE_DISH = "Dish at this restaurant with this name and date already exists";

    private DishService service;
    private GlobalControllerExceptionHandler exceptionInfoHandler;

    @Autowired
    public DishAdminRestController(DishService service) {
        this.service = service;
    }

    @Autowired
    public void setExceptionInfoHandler(GlobalControllerExceptionHandler exceptionInfoHandler) {
        this.exceptionInfoHandler = exceptionInfoHandler;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dish get(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        LOG.info("Get today Dish {} for Restaurant {}", id, restaurantId);
        return service.get(id, restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Valid @RequestBody DishTo dishTo, @PathVariable("restaurantId") int restaurantId) {
        LOG.info("Create {} for Restaurant {}", dishTo, restaurantId);
        checkNew(dishTo);
        Dish created = service.save(dishTo, restaurantId);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        LOG.info("Update {} with id {} for Restaurant {}", dishTo, id, restaurantId);
        checkIdConsistent(dishTo, id);
        service.update(dishTo, restaurantId);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id, @PathVariable("restaurantId") int restaurantId) {
        LOG.info("Delete Dish {} for Restaurant {}", id, restaurantId);
        service.delete(id, restaurantId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> getAll(@PathVariable("restaurantId") int restaurantId) {
        LOG.info("Get all today dishes for Restaurant {}", restaurantId);
        return service.getAll(restaurantId, LocalDate.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> duplicateEmailException(HttpServletRequest req, DataIntegrityViolationException e) {
        return exceptionInfoHandler.getErrorInfoResponseEntity(req, e, EXCEPTION_DUPLICATE_DISH, HttpStatus.CONFLICT);
    }

}
