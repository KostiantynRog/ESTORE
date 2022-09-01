package com.rog.EShop.repository;

import com.rog.EShop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findFirst5ByOrderByIdDesc(Pageable pageable);
    Page<Item> findAllByCategoryId(Integer id, Pageable pageable);

}
