package com.pragma.home360.home.shared.configurations.beans;


import com.pragma.home360.home.application.mappers.*;
import com.pragma.home360.home.application.services.*;
import com.pragma.home360.home.application.services.impl.*;
import com.pragma.home360.home.domain.ports.in.*;
import com.pragma.home360.home.domain.ports.out.*;
import com.pragma.home360.home.domain.usecases.*;
import com.pragma.home360.home.infrastructure.adapters.persistence.*;
import com.pragma.home360.home.infrastructure.mappers.*;
import com.pragma.home360.home.infrastructure.repositories.mysql.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;


    @Bean
    public CityPersistencePort cityPersistencePort(
            CityRepository cityRepository,
            CityEntityMapper cityEntityMapper) {
        return new CityPersistenceAdapter(cityRepository, cityEntityMapper);
    }

    @Bean
    public CityServicePort cityServicePort(CityPersistencePort cityPersistencePort, DepartmentPersistencePort departmentPersistencePort) {
        return new CityUseCase(cityPersistencePort, departmentPersistencePort);
    }

    @Bean
    public CityService cityService(CityServicePort cityServicePort, CityDtoMapper cityDtoMapper) {
        return new CityServiceImpl(cityServicePort, cityDtoMapper);
    }

    @Bean
    public DepartmentPersistencePort departmentPersistencePort(
            DepartmentRepository departmentRepository,
            DepartmentEntityMapper departmentEntityMapper) {
        return new DepartmentPersistenceAdapter(departmentRepository, departmentEntityMapper);
    }

    @Bean
    public DepartmentServicePort departmentServicePort(DepartmentPersistencePort departmentPersistencePort) {
        return new DepartmentUseCase(departmentPersistencePort);
    }

    @Bean
    public DepartmentService departmentService(
            DepartmentServicePort departmentServicePort,
            DepartmentDtoMapper departmentDtoMapper) {
        return new DepartmentServiceImpl(departmentServicePort, departmentDtoMapper);
    }


    @Bean
    public LocationPersistencePort locationPersistencePort(
            LocationRepository locationRepository,
            LocationEntityMapper locationEntityMapper) {
        return new LocationPersistenceAdapter(locationRepository, locationEntityMapper);
    }


    @Bean
    public LocationServicePort locationServicePort(
            LocationPersistencePort locationPersistencePort,
            NeighborhoodPersistencePort neighborhoodPersistencePort,
            CityPersistencePort cityPersistencePort
    ) {
        return new LocationUseCase(
                locationPersistencePort,
                neighborhoodPersistencePort,
                cityPersistencePort
        );
    }


    @Bean
    public LocationService locationService(
            LocationServicePort locationServicePort,
            LocationDtoMapper locationDtoMapper) {
        return new LocationServiceImpl(locationServicePort, locationDtoMapper);
    }


    @Bean
    public CategoryServicePort categoryServicePort() {
        return new CategoryUseCase(categoryPersistencePort());
    }

    @Bean
    CategoryPersistencePort categoryPersistencePort() {
        return new CategoryPersistenceAdapter(categoryRepository, categoryEntityMapper);
    }


    @Bean
    public NeighborhoodPersistencePort neighborhoodPersistencePort(
            NeighborhoodRepository neighborhoodRepository,
            NeighborhoodEntityMapper neighborhoodEntityMapper) {
        return new NeighborhoodPersistenceAdapter(neighborhoodRepository, neighborhoodEntityMapper);
    }

    @Bean
    public NeighborhoodServicePort neighborhoodServicePort(
            NeighborhoodPersistencePort neighborhoodPersistencePort,
            CityPersistencePort cityPersistencePort) {
        return new NeighborhoodUseCase(neighborhoodPersistencePort, cityPersistencePort);
    }

    @Bean
    public NeighborhoodService neighborhoodService(
            NeighborhoodServicePort neighborhoodServicePort,
            CityServicePort cityServicePort,
            NeighborhoodDtoMapper neighborhoodDtoMapper) {
        return new NeighborhoodServiceImpl(neighborhoodServicePort, cityServicePort, neighborhoodDtoMapper);
    }


    @Bean
    public PropertyService propertyService(PropertyServicePort propertyServicePort, PropertyDtoMapper propertyDtoMapper) {
        return new PropertyServiceImpl(propertyServicePort, propertyDtoMapper);
    }

    @Bean
    public PropertyServicePort propertyServicePort(
            PropertyPersistencePort propertyPersistencePort,
            LocationPersistencePort locationPersistencePort,
            CategoryPersistencePort categoryPersistencePort
    ) {

        return new PropertyUseCase(
                propertyPersistencePort,
                locationPersistencePort,
                categoryPersistencePort
        );
    }


    @Bean
    public PropertyImagePersistencePort propertyImagePersistencePort(
            PropertyImageRepository propertyImageRepository,
            PropertyRepository propRepository,
            PropertyImageEntityMapper propertyImageEntityMapper) {
        return new PropertyImagePersistenceAdapter(propertyImageRepository, propRepository, propertyImageEntityMapper);
    }

    @Bean
    public PropertyImageServicePort propertyImageServicePort(
            PropertyImagePersistencePort propertyImagePersistencePort,
            PropertyPersistencePort propertyPersistencePort,
            FileStorageService fileStorageService) {
        return new PropertyImageUseCase(propertyImagePersistencePort, propertyPersistencePort, fileStorageService);
    }

    @Bean
    public PropertyImageService propertyImageService(
            PropertyImageServicePort propertyImageServicePort,
            PropertyImageDtoMapper propertyImageDtoMapper) {
        return new PropertyImageServiceImpl(propertyImageServicePort, propertyImageDtoMapper);
    }
}
