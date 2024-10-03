package com.examplejl.truckbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TruckBackendApplication implements CommandLineRunner {

    @Autowired
    DatabaseConnectionSecrets dbSecrets;

    public static void main(String[] args) {
        SpringApplication.run(TruckBackendApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        TruckDatabase database = new TruckDatabase(dbSecrets);
        database.createTable();
        database.populateTableWithSampleData();
        database.printAllData();
    }
}
