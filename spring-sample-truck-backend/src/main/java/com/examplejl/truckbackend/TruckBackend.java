package com.examplejl.truckbackend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/truckbackend")
public class TruckBackend {

    @Autowired
    DatabaseConnectionSecrets dbSecrets;

    private TruckDatabase database;

    @PostConstruct
    public void postConstruct() {
        database = new TruckDatabase(dbSecrets);
    }

    @RequestMapping("/truck/{id}")
    public TruckInfo getTruck(@PathVariable("id") String truckId) throws SQLException {
        return database.readTruck(truckId);
    }

    @RequestMapping("/trucks/{region}/{country}")
    public Collection<TruckInfo> getAllTrucksFromRegion(@PathVariable("region") String region, @PathVariable("country") String country) throws SQLException {
        return database.findAllTrucks(region, country);
    }

    @RequestMapping("/health")
    public StringResponse getHealthOfService() {
        return new StringResponse("OK healthy");
    }

    @RequestMapping("/release")
    public StringResponse getReleaseInfo() {
        return new StringResponse("0.1");
    }

}