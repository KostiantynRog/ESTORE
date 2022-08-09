package com.rog.EShop.controllers;

import com.rog.EShop.entity.Category;
import com.rog.EShop.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/")
public class CategoryController {
    private  final CategoryService categoryService;


    @GetMapping(path = "/categories")
    public List<Category> getAllCategories(){
        return categoryService.findAll();
    }
    @GetMapping(path = "/categories/{id}")
    public Optional<Category> getCategoryById(@PathVariable Integer id){
        return categoryService.findById(id);
    }
}
