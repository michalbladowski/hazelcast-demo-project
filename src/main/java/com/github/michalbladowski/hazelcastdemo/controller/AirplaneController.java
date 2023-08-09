package com.github.michalbladowski.hazelcastdemo.controller;

import com.github.michalbladowski.hazelcastdemo.converter.AirplaneToAirplaneDtoConverter;
import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import com.github.michalbladowski.hazelcastdemo.service.AirplaneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(
        name = "Airplanes Controller",
        description = "API to manipulate persisted airplanes"
)
@RestController
@RequestMapping("/api/airplanes")
@RequiredArgsConstructor
@Slf4j
public class AirplaneController {

    @Autowired
    private final AirplaneService airplaneService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get all airplanes")
    @ApiResponse(responseCode = "200", description = "Returns all stored airplanes")
    @ApiResponse(responseCode = "404", description = "Content not found")
    public ResponseEntity<List<AirplaneDto>> getAllAirplanes() {
        log.info("AirplaneController::getAllAirplanes()");
        List<Airplane> airplaneList = airplaneService.getAllAirplanes();
        if (!airplaneList.isEmpty()) {
            return ResponseEntity.ok(airplaneList.stream()
                    .map(AirplaneToAirplaneDtoConverter::convertAirplaneToAirplaneDto)
                    .toList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get airplane by name")
    @ApiResponse(responseCode = "200", description = "Returns requested airplane")
    @ApiResponse(responseCode = "404", description = "Airplane not found")
    public ResponseEntity<AirplaneDto> getAirplaneByName(@RequestParam String modelName) {
        log.info("AirplaneController::getAirplaneByName()");
        Airplane response = airplaneService.getAirplaneByName(modelName);
        if (null != response) {
            return ResponseEntity.ok(AirplaneToAirplaneDtoConverter.convertAirplaneToAirplaneDto(response));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Add new airplane")
    @ApiResponse(responseCode = "201", description = "Successfully added new airplane")
    @ApiResponse(responseCode = "422", description = "Unable to add new airplane")
    public ResponseEntity<Object> addNewAirplane(@Valid @RequestBody AirplaneDto airplaneDto) {
        log.info("AirplaneController::addNewAirplane()");
        Airplane response = airplaneService.saveAirplane(airplaneDto);
        if (null != response) {
            log.info("AirplaneController::addNewAirplane() - Airplane added successfully");
            return ResponseEntity.created(URI.create("/airplanes?modelName=" + response.getModelName())).build();
        } else {
            log.info("AirplaneController::addNewAirplane() - Airplane adding failed");
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping(value = "/delete/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Delete airplane with given id")
    @ApiResponse(responseCode = "200", description = "Successfully deleted the airplane")
    public ResponseEntity<Object> deleteGivenAirplane(@PathVariable long id) {
        log.info("AirplaneController::deleteGivenAirplane()");
        airplaneService.deleteAirplaneById(id);
        return ResponseEntity.ok().build();
    }
}
