package com.pragma.home360.home.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "property_images")
@Entity
public class PropertyImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "is_main_image", nullable = false)
    private Boolean isMainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private PropertyEntity property;
}
