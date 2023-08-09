package com.github.michalbladowski.hazelcastdemo.dto;

import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class AirplaneDto {
    private final String modelName;
    private final Manufacturer manufacturer;
    private final LocalDate firstFlight;
}
