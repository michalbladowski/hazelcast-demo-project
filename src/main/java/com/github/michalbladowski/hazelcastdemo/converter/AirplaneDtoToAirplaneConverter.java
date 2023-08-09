package com.github.michalbladowski.hazelcastdemo.converter;

import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AirplaneDtoToAirplaneConverter {
    public Airplane convertAirplaneDtoToAirplane(AirplaneDto airplaneDto) {
        if(null != airplaneDto) {
            return Airplane.builder()
                    .modelName(airplaneDto.getModelName())
                    .manufacturer(airplaneDto.getManufacturer())
                    .firstFlight(airplaneDto.getFirstFlight())
                    .build();
        }
        return null;
    }
}
