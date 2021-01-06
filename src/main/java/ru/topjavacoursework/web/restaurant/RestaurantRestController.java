package ru.topjavacoursework.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.topjavacoursework.AuthorizedUser;
import ru.topjavacoursework.model.Restaurant;
import ru.topjavacoursework.service.RestaurantService;
import ru.topjavacoursework.service.VoteService;
import ru.topjavacoursework.util.exception.ErrorInfo;
import ru.topjavacoursework.util.exception.LateVoteException;
import ru.topjavacoursework.web.GlobalControllerExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = RestaurantRestController.REST_URL)
public class RestaurantRestController {
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantRestController.class);

    static final String REST_URL = "/api/restaurants";

    private final RestaurantService service;
    private final VoteService voteService;

    private GlobalControllerExceptionHandler exceptionInfoHandler;

    @Autowired
    public RestaurantRestController(RestaurantService service, VoteService voteService) {
        this.service = service;
        this.voteService = voteService;
    }

    @Autowired
    public void setExceptionInfoHandler(GlobalControllerExceptionHandler exceptionInfoHandler) {
        this.exceptionInfoHandler = exceptionInfoHandler;
    }

    @PostMapping(value = "/{id}/vote")
    public void vote(@PathVariable("id") int id, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        LOG.info("Vote User {} for Restaurant {}", authorizedUser.getId(), id);
        voteService.vote(authorizedUser.getId(), id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAll(@RequestParam(value = "withDishes", required = false, defaultValue = "true") boolean withDishes) {
        LOG.info("Get all restaurants withDishes = {}", withDishes);
        if (withDishes) {
            return service.getAllWithDishes(LocalDate.now());
        } else {
            return service.getAll();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant get(@PathVariable("id") int id,
                          @RequestParam(value = "withDishes", required = false, defaultValue = "true") boolean withDishes) {
        LOG.info("Get Restaurant {} withDishes = {}", id, withDishes);
        if (withDishes) {
            return service.getWithDishes(id, LocalDate.now());
        } else {
            return service.get(id);
        }
    }

    @ExceptionHandler(LateVoteException.class)
    public ResponseEntity<ErrorInfo> lateVoteException(HttpServletRequest req, LateVoteException e) {
        return exceptionInfoHandler.getErrorInfoResponseEntity(req, e, e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
