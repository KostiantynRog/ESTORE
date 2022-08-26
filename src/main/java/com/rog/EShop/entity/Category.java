package com.rog.EShop.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "CATEGORIES")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_id_generator")
    @SequenceGenerator(name = "categories_id_generator", sequenceName = "categories_id_seq", allocationSize = 1)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Item> items;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
