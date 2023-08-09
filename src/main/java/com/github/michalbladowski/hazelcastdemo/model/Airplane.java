package com.github.michalbladowski.hazelcastdemo.model;

import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "airplanes")
public class Airplane implements Serializable {

    @Serial
    private static final long serialVersionUID = -4950306871853569222L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "model_name")
    private String modelName;

    @Column
    @Enumerated(EnumType.STRING)
    private Manufacturer manufacturer;

    @Column(name = "first_flight")
    private LocalDate firstFlight;
}
