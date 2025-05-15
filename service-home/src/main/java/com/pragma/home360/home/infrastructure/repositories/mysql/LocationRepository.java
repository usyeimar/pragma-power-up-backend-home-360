package com.pragma.home360.home.infrastructure.repositories.mysql;

import com.pragma.home360.home.infrastructure.entities.LocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    /**
     * Busca ubicaciones por texto en nombre de ciudad o departamento
     * La consulta utiliza LOWER para ignorar mayúsculas/minúsculas y contiene
     * para encontrar coincidencias parciales.
     *
     * @param searchText Texto de búsqueda normalizado
     * @param pageable   Configuración de paginación y ordenamiento
     * @return Página de ubicaciones que coinciden con la búsqueda
     */
    @Query(
            value = "SELECT l FROM LocationEntity l " +
                    "WHERE (:searchText IS NULL OR :searchText = '' OR " +
                    "LOWER(l.referencePoint) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.address) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.neighborhood.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.neighborhood.city.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.neighborhood.city.department.name) LIKE LOWER(CONCAT('%', :searchText, '%')))",

            countQuery = "SELECT COUNT(l) FROM LocationEntity l " +
                    "WHERE (:searchText IS NULL OR :searchText = '' OR " +
                    "LOWER(l.referencePoint) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.address) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.neighborhood.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.neighborhood.city.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                    "LOWER(l.neighborhood.city.department.name) LIKE LOWER(CONCAT('%', :searchText, '%')))"
    )
    Page<LocationEntity> searchLocations(@Param("searchText") String searchText, Pageable pageable);

    boolean existsByAddressAndNeighborhoodId(String address, Long id);

}