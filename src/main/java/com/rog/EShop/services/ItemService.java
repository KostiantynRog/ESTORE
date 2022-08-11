package com.rog.EShop.services;

import com.rog.EShop.entity.Item;
import com.rog.EShop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Optional<Item> findById(Integer id) {
        return itemRepository.findById(id);
    }

    public List<Item> findFirst5By() {
        return itemRepository.findFirst5By();
    }

    public List<Item> findAllByCategoryId(Integer id) {
        return itemRepository.findAllByCategoryId(id);
    }
}
