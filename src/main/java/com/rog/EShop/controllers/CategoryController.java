package com.rog.EShop.controllers;

import com.rog.EShop.dto.CategoryCreateDto;
import com.rog.EShop.entity.Category;
import com.rog.EShop.mapper.EntityMapper;
import com.rog.EShop.services.CategoryService;
import org.springframework.http.HttpStatus;
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
    public List<CategoryCreateDto> getAllCategories() {
       List<Category> categories = categoryService.findAll();
        return EntityMapper.INSTANCE.toDTO( categories);
    }

    @GetMapping(path = "/categories/{id}")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public CategoryCreateDto getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);
        return EntityMapper.INSTANCE.toDTO(category);
    }

    @PostMapping(path = "/categories")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Category create(@RequestBody Category category) {
        if (category.getId() != null) {
            throw new RuntimeException("A new category cannot already have an ID");
        }

        return categoryService.save(category);
    }

    @PutMapping(path = "/categories")
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
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
