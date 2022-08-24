package com.rog.EShop.controllers;

import com.rog.EShop.entity.Item;
import com.rog.EShop.services.ItemService;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Optional<Item> getItemById(@PathVariable Integer id) {
        return itemService.findById(id);
    }

    @GetMapping(path = "/items/last")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public List<Item> getFirst5By() {
        return itemService.findFirst5By();
    }


    @PostMapping(path = "/items")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Item create(@RequestBody Item item) {
        if (item.getCategory() == null) {
            throw new RuntimeException("A new item should have category ID");

        }

        return itemService.save(item);
    }

    @PutMapping(path = "/items")
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Item update(@RequestBody Item item) {
        if (item.getId() == null) {
            throw new RuntimeException(" Id is not present in request body");
        }
        return itemService.update(item);
    }


    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.delete(id);
    }
}
