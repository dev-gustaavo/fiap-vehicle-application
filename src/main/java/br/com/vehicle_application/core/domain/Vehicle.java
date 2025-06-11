package br.com.vehicle_application.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Vehicle {

    private Integer id;
    private String brand;
    private String model;
    private String modelYear;
    private String factoryYear;
    private String color;
    private BigDecimal price;
    private String marketReferenceCode;
    private boolean toSell;
    private int quantityAvailable;
}
