package com.pragma.home360.home.infrastructure.entities;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private Double latitude;
    private Double longitude;
    private String referencePoint;

    @ManyToOne
    @JoinColumn(name = "neighborhood_id")
    private NeighborHoodEntity neighborhood;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityEntity city;

}

