package com.rog.EShop.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    @OneToMany(mappedBy = "ITEMS")
    private List<Category> categories;
//    private Integer category_id;
}
