package com.rog.EShop.controllers;

import com.rog.EShop.entity.Category;
import com.rog.EShop.services.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping(path = "/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "/categories")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping(path = "/categories/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @PostMapping(path = "/categories")
    public Category create(@RequestBody Category category) {
        if (category.getId() != null) {
            throw new RuntimeException("A new category cannot already have an ID");
        }

        return categoryService.save(category);
    }

    @PutMapping(path = "/categories")
    public Category update(@RequestBody Category category) {
        if (category.getId() == null) {
            throw new RuntimeException(" Id is not present in request body");
        }
        return categoryService.update(category);
    }

    @DeleteMapping(path = "/categories/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
