package ru.topjavacoursework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Table(name = "dishes", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "date", "name"},
        name = "dishes_unique_rest_date_name_idx"))
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 1, max = 2000)
    private Integer price;

    @Column(name = "date", nullable = false, columnDefinition = "date default today()")
    @NotNull
    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @JsonBackReference
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(Dish d) {
        this(d.getId(), d.getName(), d.getRestaurant(), d.getDate(), d.getPrice());
    }

    public Dish(Integer id, String name, Restaurant restaurant, Integer price) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
    }

    public Dish(Integer id, String name, Restaurant restaurant, LocalDate date, Integer price) {
        super(id, name);
        this.restaurant = restaurant;
        this.date = date;
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + getId() +
                ", name=" + name +
                ", price=" + price +
                ", date=" + date +
                '}';
    }

}
