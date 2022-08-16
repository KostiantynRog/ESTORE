package com.rog.EShop.controllers;

import com.rog.EShop.entity.Item;
import com.rog.EShop.services.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping(path = "/api")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

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

    @PostMapping(path = "/items")
    public Item create(@RequestBody Item item) {
        return itemService.save(item);
    }

    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.delete(id);
    }
}
