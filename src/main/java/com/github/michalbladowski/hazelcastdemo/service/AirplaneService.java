package com.github.michalbladowski.hazelcastdemo.service;

import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;

import java.util.List;

public interface AirplaneService {
    List<Airplane> getAllAirplanes();
    List<Airplane> getAirplanesByManufacturer(Manufacturer manufacturer);
    Airplane getAirplaneById(long id);
    Airplane getAirplaneByName(String modelName);
    Airplane saveAirplane(AirplaneDto airplaneDto);
    void deleteAirplane(AirplaneDto airplaneDto);
    void deleteAirplaneById(long id);
}
