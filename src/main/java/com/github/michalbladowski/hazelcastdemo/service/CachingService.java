package com.github.michalbladowski.hazelcastdemo.service;

import com.github.michalbladowski.hazelcastdemo.model.Airplane;

import java.util.List;

public interface CachingService {
    List<Airplane> getCacheContent();
    Airplane getEntryFromCache(String key);
    Airplane getEntryFromCache(long id);
    Airplane addToCache(String key, Airplane value);
    void deleteByName(String modelName);
    void deleteById(long id);
    void evictAll();
}
