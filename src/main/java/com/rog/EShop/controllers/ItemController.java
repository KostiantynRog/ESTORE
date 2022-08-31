package com.rog.EShop.controllers;

import com.rog.EShop.dto.ItemDto;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class ItemController {
    private final Logger log = LoggerFactory.getLogger(ItemController.class);
    private final ItemService itemService;


    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(path = "/items/{id}")
    public ItemDto getItemById(@PathVariable Integer id) {
        log.debug("Find item by itemId - {}", id);
        return itemService.findById(id);
    }

    @GetMapping(path = "/items/last")
    public List<ItemDto> getLast5By(Integer pageNo, Integer pageSize) {
        return itemService.findLast5By(pageNo, pageSize);
    }

    @GetMapping(path = "/items")
    public List<ItemDto> getAllByCategoryId(@RequestParam("categoryId") Integer id,
                                            @RequestParam(defaultValue = "0") Integer pageNo,
                                            @RequestParam(defaultValue = "5") Integer pageSize,
                                            @RequestParam(defaultValue = "name") String name) {
        return itemService.findAllByCategoryId(id, pageNo, pageSize, name);
    }

    @PostMapping(path = "/items")
    public ResponseEntity<ItemDto> create(@RequestBody ItemDto itemDto) {
        if (itemDto.getId() != null) {
            throw new BadRequestException("Id should be empty");
        }
        ItemDto itemDtoNew = itemService.save(itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDtoNew);
    }

    @PutMapping(path = "/items")
    public ItemDto update(@RequestBody ItemDto itemDto) {
        if (itemDto.getId() == null) {
            log.error("Not found itemDtoId");
            throw new RuntimeException(" Id is not present in request body");
        }
        return itemService.update(itemDto);

    }


    @DeleteMapping(path = "/items/{id}")
    public void deleteItem(@PathVariable Integer id) {
        itemService.delete(id);
    }
}
