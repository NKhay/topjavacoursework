package ru.topjavacoursework.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "Restaurants", uniqueConstraints = @UniqueConstraint(name = "restaurants_unique_name_idx", columnNames = {"name"}))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("name ASC")
    @BatchSize(size = 300)
    @JsonManagedReference
    private List<Dish> dishes;

    public Restaurant() {
    }

    public Restaurant(Restaurant r) {
        super(r.getId(), r.getName());
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public List<Dish> getDishes() {
        return dishes;
    }

}
