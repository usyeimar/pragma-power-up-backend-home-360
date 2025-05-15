package com.pragma.home360.home.domain.model;

public class LocationModel {
    private Long id;
    private String address;
    private Double latitude;
    private Double longitude;
    private String referencePoint;
    private Long neighborhoodId;
    private String neighborhoodName;
    private Long cityId;
    private String cityName;
    private Long departmentId;
    private String departmentName;

    public LocationModel() {
    }

    public LocationModel(Long id, String address, Double latitude, Double longitude,
                         String referencePoint, Long neighborhoodId,Long cityId) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.referencePoint = referencePoint;
        this.neighborhoodId = neighborhoodId;
        this.cityId = cityId;
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

    public Long getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(Long neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public String getNeighborhoodName() {
        return neighborhoodName;
    }

    public void setNeighborhoodName(String neighborhoodName) {
        this.neighborhoodName = neighborhoodName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    @Override
    public String toString() {
        return "LocationModel{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", referencePoint='" + referencePoint + '\'' +
                ", neighborhoodId=" + neighborhoodId +
                ", neighborhoodName='" + neighborhoodName + '\'' +
                ", cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", neighborhoodId=" + neighborhoodId + '\n' +
                ", cityId=" + cityId + '\n' +
                '}';
    }
}