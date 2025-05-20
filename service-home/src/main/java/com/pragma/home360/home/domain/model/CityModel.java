package com.pragma.home360.home.domain.model;


public class CityModel {
    private Long id;
    private String name;
    private String description;
    private Long departmentId;
    private DepartmentModel department;

    public CityModel(Long id, String name, String description, DepartmentModel department) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.department = department;
    }

    public CityModel() {
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public DepartmentModel getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentModel department) {
        this.department = department;
    }


    @Override
    public String toString() {

        return "CityModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}




