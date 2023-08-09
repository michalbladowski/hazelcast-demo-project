package com.github.michalbladowski.hazelcastdemo.service;

import com.github.michalbladowski.hazelcastdemo.converter.AirplaneDtoToAirplaneConverter;
import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import com.github.michalbladowski.hazelcastdemo.repository.AirplaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {

    @Autowired
    private final CachingService cachingService;

    @Autowired
    private final AirplaneRepository airplaneRepository;

    @Override
    public List<Airplane> getAllAirplanes() {
        return cachingService.getCacheContent();
    }

    @Override
    public List<Airplane> getAirplanesByManufacturer(Manufacturer manufacturer) {
        return airplaneRepository.findByManufacturer(manufacturer);
    }

    @Override
    public Airplane getAirplaneById(long id) {
        // return airplaneRepository.findByModelName(modelName).orElse(null);
        return cachingService.getEntryFromCache(id);
    }

    @Override
    public Airplane getAirplaneByName(String modelName) {
       // return airplaneRepository.findByModelName(modelName).orElse(null);
        return cachingService.getEntryFromCache(modelName);
    }

    @Override
    public Airplane saveAirplane(AirplaneDto airplaneDto) {
        if(null != airplaneDto) {
            //return airplaneRepository.save(AirplaneDtoToAirplaneConverter.convertAirplaneDtoToAirplane(airplaneDto));
            return cachingService.addToCache(airplaneDto.getModelName(), AirplaneDtoToAirplaneConverter.convertAirplaneDtoToAirplane(airplaneDto));
        } else {
            return null;
        }
    }

    @Override
    public void deleteAirplaneById(long id) {
        if(id >= 0) {
            //airplaneRepository.deleteById(id);
            cachingService.deleteById(id);
        } else {
            throw new IllegalArgumentException("Provided ID is not valid");
        }
    }

    @Override
    public void deleteAirplane(AirplaneDto airplaneDto) {
        if(null != airplaneDto) {
            //airplaneRepository.delete(AirplaneDtoToAirplaneConverter.convertAirplaneDtoToAirplane(airplaneDto));
            cachingService.deleteByName(airplaneDto.getModelName());
        } else {
            throw new IllegalArgumentException("Provided entity is empty");
        }
    }
}
