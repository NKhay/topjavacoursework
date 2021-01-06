package ru.topjavacoursework.model;

import javax.validation.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public class NamedEntity extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    protected String name;

    public NamedEntity() {
    }

    public NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Entity %s (%s, '%s')", getClass().getName(), getId(), name);
    }

}
