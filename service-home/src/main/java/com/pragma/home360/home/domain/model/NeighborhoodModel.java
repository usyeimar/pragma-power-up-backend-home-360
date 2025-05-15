package com.pragma.home360.home.domain.model;

public class NeighborhoodModel {
    private Long id;
    private String name;
    private String description;
    private Long cityId;

    public NeighborhoodModel() {
    }

    public NeighborhoodModel(Long id, String name, String description, Long cityId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cityId = cityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "NeighborhoodModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cityId=" + cityId +
                '}';
    }
}