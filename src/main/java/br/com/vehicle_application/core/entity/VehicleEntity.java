package br.com.vehicle_application.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@Accessors(chain = true)
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String modelYear;

    @Column(nullable = false)
    private String factoryYear;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String marketReferenceCode;

    @Column(nullable = false)
    private boolean toSell;

    @Column
    private Integer quantityAvailable;
}
