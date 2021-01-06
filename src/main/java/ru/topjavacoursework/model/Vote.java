package ru.topjavacoursework.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Table(name = "votes", uniqueConstraints = @UniqueConstraint(columnNames = {"date", "user_id", "restaurant_id"},
        name = "votes_unique_date_user_restaurant_idx"))
public class Vote extends BaseEntity {

    @Column(name = "date", nullable = false, columnDefinition = "date default today()")
    @NotNull
    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(LocalDate date) {
        this(null, null, null, date);
    }

    public Vote(Vote vote) {
        this(vote.getId(), vote.getUser(), vote.getRestaurant(), vote.getDate());
    }

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate date) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
