package com.pragma.home360.home.infrastructure.entities;

import com.pragma.home360.home.infrastructure.entities.enums.PropertyPublicationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "properties")
public class PropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;


    @Column(name = "number_of_rooms", nullable = false)
    private Integer numberOfRooms;

    @Column(name = "number_of_bathrooms", nullable = false)
    private Integer numberOfBathrooms;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "active_publication_date")
    private LocalDate activePublicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_status", nullable = false, length = 50)
    private PropertyPublicationStatus publicationStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToMany
    @JoinColumn(name = "property_id")
    private List<PropertyImageEntity> images;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "PropertyEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", price=" + price +
                ", activePublicationDate=" + activePublicationDate +
                ", publicationStatus=" + publicationStatus +
                ", location=" + location +
                ", category=" + category +
                ", images=" + images +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
