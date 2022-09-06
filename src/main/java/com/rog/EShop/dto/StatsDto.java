package com.rog.EShop.dto;

public class StatsDto {
    private String category;
    private long itemsCount;

    public StatsDto(String category, long itemsCount) {
        this.category = category;
        this.itemsCount = itemsCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(long itemsCount) {
        this.itemsCount = itemsCount;
    }
}
