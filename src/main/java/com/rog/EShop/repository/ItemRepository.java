package com.rog.EShop.repository;

import com.rog.EShop.dto.StatsDto;
import com.rog.EShop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findAllByCategoryId(Integer id, Pageable pageable);

    @Query("select new com.rog.EShop.dto.StatsDto(c.name, count(i.id)) from Category c" +
            " LEFT JOIN  c.items i group by c.id")
    List<StatsDto> getStats();

//    @Query("select i from Item i where lower(i.name) like lower(concat('%', :filter, '%') ) ")
//    List<Item> findByName(String filter);

//    @Query(value = "select * from items i where lower(i.name) like lower(concat('%', :filter, '%') )", nativeQuery = true)
//    List<Item> findByName(String filter);

    List<Item> findByNameContainingIgnoreCase(String filter);

}
