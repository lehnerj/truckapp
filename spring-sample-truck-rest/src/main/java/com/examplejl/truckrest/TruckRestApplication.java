package com.examplejl.truckrest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TruckRestApplication implements CommandLineRunner {

    @Autowired
    DatabaseConnectionSecrets dbSecrets;

    public static void main(String[] args) {
        SpringApplication.run(TruckRestApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        AuditDatabase database = new AuditDatabase(dbSecrets);
        database.createTable();
    }
}
