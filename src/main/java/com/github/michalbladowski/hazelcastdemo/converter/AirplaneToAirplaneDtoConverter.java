package com.github.michalbladowski.hazelcastdemo.converter;

import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AirplaneToAirplaneDtoConverter {
    public AirplaneDto convertAirplaneToAirplaneDto(Airplane airplane) {
        if(null != airplane) {
            return AirplaneDto.builder()
                    .modelName(airplane.getModelName())
                    .manufacturer(airplane.getManufacturer())
                    .firstFlight(airplane.getFirstFlight())
                    .build();
        }
        return null;
    }
}
