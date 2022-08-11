package com.rog.EShop.controllers;

import com.rog.EShop.entity.Category;
import com.rog.EShop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class CategoryController {
    private final CategoryService categoryService;


    @GetMapping(path = "/categories")
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping(path = "/categories/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @PostMapping(path = "/categories")
    public Category create(@RequestBody Category newCategory) {
        return categoryService.save(newCategory);
    }

//    @PutMapping(path = "/categories/{id}")
//    public Category update(@PathVariable Integer id, @RequestBody Category categoryDetails){
//       Optional<Category> category = categoryService.findById(id);
//
//
//    }
}
