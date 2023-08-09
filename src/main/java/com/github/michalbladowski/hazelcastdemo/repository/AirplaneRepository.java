package com.github.michalbladowski.hazelcastdemo.repository;

import com.github.michalbladowski.hazelcastdemo.enums.Manufacturer;
import com.github.michalbladowski.hazelcastdemo.model.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    List<Airplane> findByManufacturer(Manufacturer manufacturer);
    Optional<Airplane> findByModelName(String modelName);
}
