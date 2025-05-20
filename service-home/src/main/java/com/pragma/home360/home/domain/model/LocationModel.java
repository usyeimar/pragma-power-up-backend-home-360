package com.pragma.home360.home.domain.model;

public class LocationModel {
    private Long id;
    private String address;
    private Double latitude;
    private Double longitude;
    private String referencePoint;

    private NeighborhoodModel neighborhood;
    private CityModel city;
    private DepartmentModel department;

    public LocationModel() {
    }

    public LocationModel(Long id, String address, Double latitude, Double longitude,
                         String referencePoint, NeighborhoodModel neighborhood, CityModel city, DepartmentModel department) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.referencePoint = referencePoint;
        this.neighborhood = neighborhood;
        this.city = city;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public NeighborhoodModel getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(NeighborhoodModel neighborhood) {
        this.neighborhood = neighborhood;
    }

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public DepartmentModel getDepartment() {
        return department;
    }


    @Override
    public String toString() {
        return "LocationModel{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", referencePoint='" + referencePoint + '\'' +
                ", neighborhood=" + neighborhood +
                ", city=" + city +
                ", department=" + department +
                '}';
    }
}