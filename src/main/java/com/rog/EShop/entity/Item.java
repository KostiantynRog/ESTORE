package com.rog.EShop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_generator")
    @SequenceGenerator(name = "item_id_generator", sequenceName = "item_id_seq")
    private Integer id;
    private String name;
    @Column(name = "category_id",insertable = false,updatable = false)
    private Integer category_id;
    private String shortDescription;
    private String fullDescription;
    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Category category;

    public Item() {
    }

    public Item(Integer id, String name, Integer category_id, String shortDescription, String fullDescription, Category category) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.category = category;
    }

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

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
