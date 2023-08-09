package com.github.michalbladowski.hazelcastdemo.controller;

import com.github.michalbladowski.hazelcastdemo.converter.AirplaneDtoToAirplaneConverter;
import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import com.github.michalbladowski.hazelcastdemo.service.CachingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Cache Controller",
        description = "API to manipulate cache content"
)
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
@Slf4j
public class CacheController {

    @Autowired
    private final CachingService cachingService;

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get entire cache content")
    @ApiResponse(responseCode = "200", description = "Returns cache content")
    @ApiResponse(responseCode = "404", description = "Cache content not found")
    public ResponseEntity<List<Airplane>> getCacheContent() {
        log.info("CacheController::getCacheContent()");
        List<Airplane> cacheContentResponse = cachingService.getCacheContent();
        if(!cacheContentResponse.isEmpty()) {
            return ResponseEntity.ok(cacheContentResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "put", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Add item to the cache")
    @ApiResponse(responseCode = "201", description = "Successfully added item to the cache")
    @ApiResponse(responseCode = "422", description = "Unable to add new object to the cache")
    public ResponseEntity<Airplane> addToCache(@Valid @RequestBody AirplaneDto airplane) {
        log.info("CacheController::addToCache()");
        Airplane response = cachingService.addToCache(airplane.getModelName(), AirplaneDtoToAirplaneConverter.convertAirplaneDtoToAirplane(airplane));
        if (null != response) {
            return ResponseEntity.accepted().body(response);
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void flushCache() {
        cachingService.evictAll();
    }
}
