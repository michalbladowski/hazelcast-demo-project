package com.github.michalbladowski.hazelcastdemo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.michalbladowski.hazelcastdemo.converter.AirplaneToAirplaneDtoConverter;
import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import com.github.michalbladowski.hazelcastdemo.service.AirplaneService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;

@WebMvcTest(AirplaneController.class)
@WithMockUser
@Slf4j
class AirplaneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirplaneService airplaneService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());
    }

    @Test
    void testGetAllAirplanes() throws Exception {
        when(airplaneService.getAllAirplanes()).thenReturn(Collections.singletonList(createAirplane()));

        mockMvc.perform(
                get("/api/airplanes/all")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk());
    }

    @Test
    void testGetAirplaneByName() throws Exception {
        when(airplaneService.getAirplaneByName(any())).thenReturn(createAirplane());

        mockMvc.perform(
                        get("/api/airplanes/get?modelName={}", "A320")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void testAddNewAirplane() throws Exception {
        when(airplaneService.saveAirplane(any())).thenReturn(createAirplane());

        mockMvc.perform(
                        post("/api/airplanes/add")
                                .with(csrf())
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(airplaneDtoToJson(AirplaneToAirplaneDtoConverter.convertAirplaneToAirplaneDto((createAirplane()))))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    private Airplane createAirplane() {
        return Airplane.builder()
                .id(123L)
                .modelName("A320")
                .manufacturer(Manufacturer.AIRBUS)
                .firstFlight(LocalDate.parse("1987-02-22"))
                .build();
    }

    private String airplaneDtoToJson(AirplaneDto airplaneDto) {
        try {
            return objectMapper.writeValueAsString(airplaneDto);
        } catch (JsonProcessingException e) {
            log.error("Problem converting javaObject={} to json", airplaneDto);
            throw new RuntimeException(e);
        }
    }
}
