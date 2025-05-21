package com.pragma.home360.home.infrastructure.adapters.persistence;

import com.pragma.home360.home.domain.model.PropertyFilterModel;
import com.pragma.home360.home.domain.model.PropertyModel;
import com.pragma.home360.home.domain.ports.out.PropertyPersistencePort;
import com.pragma.home360.home.domain.utils.pagination.PagedResult;
import com.pragma.home360.home.domain.utils.pagination.PaginationUtils;
import com.pragma.home360.home.infrastructure.entities.CategoryEntity;
import com.pragma.home360.home.infrastructure.entities.LocationEntity;
import com.pragma.home360.home.infrastructure.entities.PropertyEntity;
import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;
import com.pragma.home360.home.infrastructure.mappers.PropertyEntityMapper;
import com.pragma.home360.home.infrastructure.repositories.mysql.CategoryRepository;
import com.pragma.home360.home.infrastructure.repositories.mysql.LocationRepository;
import com.pragma.home360.home.infrastructure.repositories.mysql.PropertyRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // Para StringUtils.hasText

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PropertyPersistenceAdapter implements PropertyPersistencePort {
    private static final Logger log = LoggerFactory.getLogger(PropertyPersistenceAdapter.class);
    private final PropertyEntityMapper propertyEntityMapper;
    private final PropertyRepository propertyRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public PropertyModel saveProperty(PropertyModel propertyModel) {
        PropertyEntity propertyEntity = propertyEntityMapper.toEntity(propertyModel);

        if (propertyModel.getLocation() != null && propertyModel.getLocation().getId() != null) {
            LocationEntity location = locationRepository.findById(propertyModel.getLocation().getId())
                    .orElseThrow(() -> new com.pragma.home360.home.domain.exceptions.ModelNotFoundException("Location not found with id: " + propertyModel.getLocation().getId()));
            propertyEntity.setLocation(location);
        }
        if (propertyModel.getCategory() != null && propertyModel.getCategory().getId() != null) {
            CategoryEntity category = categoryRepository.findById(propertyModel.getCategory().getId())
                    .orElseThrow(() -> new com.pragma.home360.home.domain.exceptions.ModelNotFoundException("Category not found with id: " + propertyModel.getCategory().getId()));
            propertyEntity.setCategory(category);
        }

        PropertyEntity savedPropertyEntity = propertyRepository.save(propertyEntity);
        return propertyEntityMapper.toModel(savedPropertyEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PropertyModel> getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .map(propertyEntityMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResult<PropertyModel> getAllProperties(PropertyFilterModel filterModel) {
        Sort.Direction direction = StringUtils.hasText(filterModel.direction()) ?
                Sort.Direction.fromString(filterModel.direction()) :
                Sort.Direction.ASC;

        String sortField = StringUtils.hasText(filterModel.sortField()) ?
                filterModel.sortField() :
                "id";

        Pageable pageable = PaginationUtils.createPageable(
                filterModel.page(),
                filterModel.size(),
                sortField,
                direction
        );

        Specification<PropertyEntity> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            assert query != null;
            if (query.getResultType() != Long.class && query.getResultType() != long.class) { // No aplicar fetch para count queries
                root.fetch("location", JoinType.LEFT);
                root.fetch("category", JoinType.LEFT);
                root.fetch("images", JoinType.LEFT);
            }


            if (StringUtils.hasText(filterModel.searchTerm())) {
                String likePattern = "%" + filterModel.searchTerm().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("location").get("address")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("location").get("neighborhood").get("name")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("location").get("neighborhood").get("city").get("name")), likePattern)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<PropertyEntity> propertyEntityPage = propertyRepository.findAll(spec, pageable);
        return PaginationUtils.toPagedResult(propertyEntityPage.map(propertyEntityMapper::toModel));
    }

    @Override
    @Transactional
    public void updateProperty(Long id, PropertyModel propertyModel) {
        PropertyEntity existingEntity = propertyRepository.findById(id)
                .orElseThrow(() -> new com.pragma.home360.home.domain.exceptions.ModelNotFoundException("Property not found with id: " + id));

        existingEntity.setName(propertyModel.getName());
        existingEntity.setDescription(propertyModel.getDescription());
        existingEntity.setNumberOfRooms(propertyModel.getNumberOfRooms());
        existingEntity.setNumberOfBathrooms(propertyModel.getNumberOfBathrooms());
        existingEntity.setPrice(propertyModel.getPrice());
        existingEntity.setActivePublicationDate(propertyModel.getActivePublicationDate());
        existingEntity.setPublicationStatus(propertyModel.getPublicationStatus());

        if (propertyModel.getCategory() != null && propertyModel.getCategory().getId() != null) {
            CategoryEntity category = categoryRepository.findById(propertyModel.getCategory().getId())
                    .orElseThrow(() -> new com.pragma.home360.home.domain.exceptions.ModelNotFoundException("Category not found with id: " + propertyModel.getCategory().getId()));
            existingEntity.setCategory(category);
        }
        if (propertyModel.getLocation() != null && propertyModel.getLocation().getId() != null) {
            LocationEntity location = locationRepository.findById(propertyModel.getLocation().getId())
                    .orElseThrow(() -> new com.pragma.home360.home.domain.exceptions.ModelNotFoundException("Location not found with id: " + propertyModel.getLocation().getId()));
            existingEntity.setLocation(location);
        }

        propertyRepository.save(existingEntity);
    }

    @Override
    @Transactional
    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new com.pragma.home360.home.domain.exceptions.ModelNotFoundException("Property not found with id: " + id);
        }
        propertyRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPropertyById(Long id) {
        return propertyRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPropertyByName(String name) {
        return propertyRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyModel> findByPublicationStatusAndActiveDateLessThanEqual(PropertyPublicationStatus status, LocalDate date) {
        List<PropertyEntity> entities = propertyRepository.findAllByPublicationStatusAndActivePublicationDateLessThanEqual(status, date);
        return entities.stream()
                .map(propertyEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateProperties(List<PropertyModel> propertyModels) {
        if (propertyModels == null || propertyModels.isEmpty()) {
            return;
        }

        List<Long> propertyIds = propertyModels.stream()
                .map(PropertyModel::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (propertyIds.isEmpty()) {
            return;
        }

        Map<Long, PropertyEntity> entityMap = propertyRepository.findAllById(propertyIds).stream()
                .collect(Collectors.toMap(PropertyEntity::getId, Function.identity()));

        List<PropertyEntity> entitiesToSave = new ArrayList<>();
        for (PropertyModel model : propertyModels) {
            if (model.getId() == null) {
                continue;
            }
            PropertyEntity entity = entityMap.get(model.getId());
            if (entity != null) {
                entity.setPublicationStatus(model.getPublicationStatus());
                entitiesToSave.add(entity);
            } else {
            }
        }

        if (!entitiesToSave.isEmpty()) {
            propertyRepository.saveAll(entitiesToSave);
        }
    }

}
