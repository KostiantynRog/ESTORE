package com.rog.EShop.controllers;

import com.rog.EShop.dto.CategoryDto;
import com.rog.EShop.entity.Category;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(path = "/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "/categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping(path = "/categories/{id}")
    public CategoryDto getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @PostMapping(path = "/categories")
    public ResponseEntity<CategoryDto> create(@RequestBody Category category) {
        if (category.getId() != null) {
            throw new BadRequestException("Id should be empty");
        }
        CategoryDto categoryDTO = categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);

    }

    @PutMapping(path = "/categories")
    public CategoryDto update(@RequestBody Category category) {
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
