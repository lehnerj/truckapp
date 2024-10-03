package com.examplejl.truckrest;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.sql.SQLException;

@Service
@RestController
@RequestMapping("/truckrest")
public class TruckRest {

    private static final Logger log = LoggerFactory.getLogger(TruckRest.class);

    @Autowired
    DatabaseConnectionSecrets dbSecrets;

    private AuditDatabase database;

    private RestClient restClient;

    @Value("${truck.targetEndpoint}")
    private String targetEndpoint;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @PostConstruct
    public void postConstruct() {
        this.restClient = restClientBuilder.baseUrl("http://" + targetEndpoint + "/truckbackend").build();
        this.database = new AuditDatabase(dbSecrets);
    }

    @RequestMapping("truck/{id}")
    public String getTruck(@PathVariable("id") String truckId) throws SQLException {
        database.audit("getTruck: truckId="+truckId);
        return this.restClient.get().uri("/truck/{id}", truckId).retrieve().body(String.class);
    }

    @RequestMapping("/trucks/{region}/{country}")
    public String getAllTrucksFromRegion(@PathVariable("region") String region, @PathVariable("country") String country) throws SQLException {
        database.audit("getAllTrucksFromRegion: region="+region+" country="+country);
        return this.restClient.get().uri("/trucks/{region}/{country}", region, country).retrieve().body(String.class);
    }

    @RequestMapping("/health")
    public String health() throws SQLException {
        database.audit("health");
        return this.restClient.get().uri("/health").retrieve().body(String.class);
    }

    @RequestMapping("/release")
    public String release() throws SQLException {
        database.audit("release");
        return this.restClient.get().uri("/release").retrieve().body(String.class);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String request) throws SQLException {
        database.audit("webhook: " + request);
        log.info("webhook: " + request);
        return ResponseEntity.ok().body(request);
    }

}