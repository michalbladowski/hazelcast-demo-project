package com.github.michalbladowski.hazelcastdemo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.michalbladowski.hazelcastdemo.converter.AirplaneToAirplaneDtoConverter;
import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import com.github.michalbladowski.hazelcastdemo.repository.AirplaneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(AirplaneServiceImpl.class)
class AirplaneServiceTest {

    @Autowired
    private AirplaneService airplaneService;

    @MockBean
    AirplaneRepository airplaneRepository;

    @MockBean
    private CachingService cachingService;

    @Test
    void getAllAirplanesTest() {
        when(cachingService.getCacheContent()).thenReturn(Collections.singletonList(createAirplane()));

        List<Airplane> airplaneList = airplaneService.getAllAirplanes();

        assertNotNull(airplaneList);
        assertEquals(Collections.singletonList(createAirplane()), airplaneList);
    }

    @Test
    void getAirplanesByManufacturerTest() {
        when(airplaneRepository.findByManufacturer(any())).thenReturn(Collections.singletonList(createAirplane()));

        List<Airplane> airplaneList = airplaneService.getAirplanesByManufacturer(Manufacturer.AIRBUS);

        assertNotNull(airplaneList);
        assertEquals(Collections.singletonList(createAirplane()), airplaneList);
    }

    @Test
    void getAirplaneByIdTest() {
        when(cachingService.getEntryFromCache(anyLong())).thenReturn(createAirplane());

        Airplane airplane = airplaneService.getAirplaneById(123L);

        assertNotNull(airplane);
        assertEquals(createAirplane(), airplane);
    }

    @Test
    void getAirplaneByNameTest() {
        when(cachingService.getEntryFromCache(anyString())).thenReturn(createAirplane());

        Airplane airplane = airplaneService.getAirplaneByName("A320");

        assertNotNull(airplane);
        assertEquals(createAirplane(), airplane);
    }

    @Test
    void saveAirplaneTest() {
        when(cachingService.addToCache(anyString(), any())).thenReturn(createAirplane());

        Airplane airplane = airplaneService.saveAirplane(AirplaneToAirplaneDtoConverter.convertAirplaneToAirplaneDto(createAirplane()));

        assertNotNull(airplane);
        assertEquals(createAirplane(), airplane);
    }

    @Test
    void deleteAirplaneByIdTest() {
        airplaneService.deleteAirplaneById(123L);
        verify(cachingService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteAirplaneByNameTest() {
        airplaneService.deleteAirplane(AirplaneToAirplaneDtoConverter.convertAirplaneToAirplaneDto(createAirplane()));
        verify(cachingService, times(1)).deleteByName(anyString());
    }

    private Airplane createAirplane() {
        return Airplane.builder()
                .id(123L)
                .modelName("A320")
                .manufacturer(Manufacturer.AIRBUS)
                .firstFlight(LocalDate.parse("1987-02-22"))
                .build();
    }
}
