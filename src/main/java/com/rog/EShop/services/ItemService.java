package com.rog.EShop.services;

import com.rog.EShop.entity.Item;
import com.rog.EShop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<Item> findById(Integer id) {
        return itemRepository.findById(id);
    }

    public List<Item> findFirst5By() {
        return itemRepository.findFirst5By();
    }


    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item update(Item item) {
        return itemRepository.save(item);
    }

    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }
}
