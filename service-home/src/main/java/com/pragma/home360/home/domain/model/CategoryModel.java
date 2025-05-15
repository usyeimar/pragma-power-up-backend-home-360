package com.pragma.home360.home.domain.model;


public class CategoryModel {
    private Long id;
    private String name;
    private String description;



    public CategoryModel() {
    }

    public CategoryModel(Long id, String name, String description) {
        setName(name);
        setDescription(description);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {

        this.description = description;
    }
}