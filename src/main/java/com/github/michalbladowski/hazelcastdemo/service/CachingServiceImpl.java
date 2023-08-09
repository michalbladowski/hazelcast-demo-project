package com.github.michalbladowski.hazelcastdemo.service;

import com.github.michalbladowski.hazelcastdemo.dto.AirplaneDto;
import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import com.github.michalbladowski.hazelcastdemo.repository.AirplaneRepository;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class CachingServiceImpl implements CachingService {

    @Autowired
    private final HazelcastInstance hazelcastInstance;

    @Autowired
    private final AirplaneRepository airplaneRepository;

    private static final String CACHE_NAME = "airplanes";

    private ConcurrentMap<String, Airplane> retrieveMap() {
        return hazelcastInstance.getMap(CACHE_NAME);
    }

    @Override
    public List<Airplane> getCacheContent() {
        log.info("Getting cache content");
        Collection<Airplane> content = retrieveMap().values();
        if(!content.isEmpty()) {
            log.info("CachingService::getCacheContent - Returning cache content");
            return retrieveMap().values().stream().toList();
        } else {
            log.info("CachingService::getCacheContent - Cache is empty. Updating with DB data");
            List<Airplane> airplaneList = airplaneRepository.findAll();
            airplaneList.forEach(e -> retrieveMap().put(e.getModelName(), e));
            return airplaneList;
        }
    }

    @Override
    public Airplane getEntryFromCache(String key) {
        log.info("Getting {} from airplanes cache", key);
        return retrieveMap().get(key);
    }

    @Override
    public Airplane getEntryFromCache(long id) {
        log.info("Getting airplane with id={} from airplanes cache", id);
        List<Airplane> airplaneList = getCacheContent();
        Optional<Airplane> airplane = airplaneList.stream().filter(e -> e.getId() == id).findAny();
        if(airplane.isPresent()) {
            log.info("Airplane with id={} has been removed from airplanes cache", id);
            return retrieveMap().get(airplane.get().getModelName());
        } else {
            log.info("Airplane with id={} not found in airplanes cache", id);
            return null;
        }
    }

    @Override
    public Airplane addToCache(String key, Airplane value) {
        log.info("Adding {} to airplanes cache", key);
        return retrieveMap().put(key, value);
    }

    @Override
    public void deleteById(long id) {
        log.info("Removing airplane with id={} from airplanes cache", id);
        List<Airplane> airplaneList = getCacheContent();
        Optional<Airplane> airplane = airplaneList.stream().filter(e -> e.getId() == id).findAny();
        if(airplane.isPresent()) {
            retrieveMap().remove(airplane.get().getModelName());
            log.info("Airplane with id={} has been removed from airplanes cache", id);
        } else {
            log.info("Airplane with id={} not found in airplanes cache", id);
        }
    }

    @Override
    public void deleteByName(String modelName) {
        log.info("Removing airplane with model name={} from airplanes cache", modelName);
        retrieveMap().remove(modelName);
    }

    @Override
    public void evictAll() {
        retrieveMap().clear();
    }
}
