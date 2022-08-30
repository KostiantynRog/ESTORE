package com.rog.EShop.controllers;

import com.rog.EShop.dto.CategoryDto;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(path = "/api")
public class CategoryController {
    private final Logger log = LoggerFactory.getLogger(CategoryController.class);
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
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto categoryDto) {
        if (categoryDto.getId() != null) {
            throw new BadRequestException("Id should be empty");
        }
        CategoryDto categoryDtoNew = categoryService.save(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDtoNew);

    }

    @PutMapping(path = "/categories")
    public CategoryDto update(@RequestBody CategoryDto categoryDto) {
        if (categoryDto.getId() == null) {
            log.error("CategoryId not found");
            throw new RuntimeException(" Id is not present in request body");
        }
        return categoryService.update(categoryDto);
    }

    @DeleteMapping(path = "/categories/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
