package com.rog.EShop.repository;

import com.rog.EShop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findFirst5ByOrderByIdDesc();
    List<Item> findAllByCategoryId(Integer id);

}
