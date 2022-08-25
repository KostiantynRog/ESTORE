package com.rog.EShop.controllers;

import com.rog.EShop.dto.CategoryCreateDto;
import com.rog.EShop.entity.Category;
import com.rog.EShop.mapper.CategoryMapper;
import com.rog.EShop.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping(path = "/api")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping(path = "/categories")
    public List<CategoryCreateDto> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return categoryMapper.toDTO(categories);
    }

    @GetMapping(path = "/categories/{id}")
    public CategoryCreateDto getCategoryById(@PathVariable Integer id) {
        Optional<Category> categoryDTO = categoryService.findById(id);
        return categoryMapper.toDTO(categoryDTO);
    }

    @PostMapping(path = "/categories")
    public ResponseEntity<CategoryCreateDto> create(@RequestBody Category category) {
        if (category.getId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Category categoryDTO = categoryService.save(category);
        CategoryCreateDto newCategory = categoryMapper.toDTO(categoryDTO);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);

    }

    @PutMapping(path = "/categories")
    public CategoryCreateDto update(@RequestBody Category category) {
        if (category.getId() == null) {
            throw new RuntimeException(" Id is not present in request body");
        }
        Category categoryDTO = categoryService.update(category);
        return categoryMapper.toDTO(categoryDTO);
    }

    @DeleteMapping(path = "/categories/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
