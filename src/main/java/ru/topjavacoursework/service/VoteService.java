package ru.topjavacoursework.service;

import ru.topjavacoursework.model.Vote;
import ru.topjavacoursework.util.exception.LateVoteException;
import ru.topjavacoursework.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;


public interface VoteService {

    void vote(int userId, int restaurantId) throws LateVoteException;

    Vote getByUser(int userId, LocalDate date) throws NotFoundException;

    List<Vote> getByUser(int userId);

    List<Vote> getByRestaurant(int restaurantId, LocalDate date);

}
