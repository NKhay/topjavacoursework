package ru.topjavacoursework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjavacoursework.model.Vote;
import ru.topjavacoursework.dao.RestaurantDao;
import ru.topjavacoursework.dao.UserDao;
import ru.topjavacoursework.dao.VoteDao;
import ru.topjavacoursework.util.exception.LateVoteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
public class VoteServiceImpl implements VoteService {
    public static final LocalTime VOTE_DEADLINE = LocalTime.of(11, 00);

    private final VoteDao dao;
    private final UserDao userDao;
    private final RestaurantDao restaurantDao;

    @Autowired
    public VoteServiceImpl(VoteDao dao, UserDao userDao, RestaurantDao restaurantDao) {
        this.dao = dao;
        this.userDao = userDao;
        this.restaurantDao = restaurantDao;
    }

    private void save(int userId, int restaurantId) {
        Vote vote = new Vote();
        vote.setUser(userDao.getOne(userId));
        vote.setRestaurant(restaurantDao.getOne(restaurantId));
        dao.save(vote);
    }

    private void update(Vote vote, int userId, int restaurantId) {
        vote.setUser(userDao.getOne(userId));
        vote.setRestaurant(restaurantDao.getOne(restaurantId));
        dao.save(vote);
    }

    @Override
    @Transactional
    public void vote(int userId, int restaurantId) throws LateVoteException {
        Vote vote = getByUser(userId, LocalDate.now());
        if (vote == null) {
            save(userId, restaurantId);
        } else {
            if (LocalTime.now().isAfter(VOTE_DEADLINE)) {
                throw new LateVoteException(VOTE_DEADLINE);
            }
            update(vote, userId, restaurantId);
        }
    }

    @Override
    public Vote getByUser(int userId, LocalDate date) {
        Assert.notNull(date, "date can't be null");
        return dao.findByUserIdAndDate(userId, date);
    }

    @Override
    public List<Vote> getByUser(int userId) {
        return dao.findByUserId(userId);
    }

    @Override
    public List<Vote> getByRestaurant(int restaurantId, LocalDate date) {
        Assert.notNull(date, "date can't be null");
        return dao.findByRestaurantIdAndDate(restaurantId, date);
    }

}
