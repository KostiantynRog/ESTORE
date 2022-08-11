package com.rog.EShop.repository;

import com.rog.EShop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
//    Category update(Category category);
}
