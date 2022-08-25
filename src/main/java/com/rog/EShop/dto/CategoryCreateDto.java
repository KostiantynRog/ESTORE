package com.rog.EShop.dto;


public class CategoryCreateDto {
    private String name;
    private String description;

    public CategoryCreateDto() {
    }

    public CategoryCreateDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
