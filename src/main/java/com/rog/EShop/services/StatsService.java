package com.rog.EShop.services;

import com.rog.EShop.dto.StatsDto;
import com.rog.EShop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatsService {
    private final ItemRepository itemRepository;

    public StatsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<StatsDto> getStats() {
        return itemRepository.getStats();
    }
}
