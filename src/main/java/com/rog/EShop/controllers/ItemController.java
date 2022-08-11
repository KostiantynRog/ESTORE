package com.rog.EShop.controllers;

import com.rog.EShop.entity.Item;
import com.rog.EShop.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class ItemController {
    private final ItemService itemService;

    @GetMapping(path = "/items/{id}")
    public Optional<Item> getItemById(@PathVariable Integer id) {
        return itemService.findById(id);
    }

    @GetMapping(path = "/items/last")
    public List<Item> getFirst5By() {
        return itemService.findFirst5By();
    }

    @GetMapping(path = "/items")
    public List<Item> getAllByCategoryId(@RequestParam("categoryId") Integer categoryId) {
        return itemService.findAllByCategoryId(categoryId);
    }
}
