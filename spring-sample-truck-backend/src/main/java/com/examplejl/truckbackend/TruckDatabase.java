package com.examplejl.truckbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class TruckDatabase {

    private static final Logger log = LoggerFactory.getLogger(TruckDatabase.class);
    private final DatabaseConnectionSecrets dbSecrets;

    public TruckDatabase(DatabaseConnectionSecrets dbSecrets) {
        this.dbSecrets = dbSecrets;
    }

    private Connection getDbConnection() throws SQLException {
        return DriverManager.getConnection(dbSecrets.getDatasourceUrl() +
                "?user=" + dbSecrets.getDatasourceUser() + "&password=" + dbSecrets.getDatasourcePassword());
    }

    public void createTable() throws SQLException {
        try (Connection conn = getDbConnection()) {
            try (Statement stmt = conn.createStatement()) {
                log.info("Creating TRUCK table");
                stmt.execute("DROP TABLE IF EXISTS truck");
                stmt.execute("CREATE TABLE truck(truckid VARCHAR(255), truckname VARCHAR(255))");
            }
        }
    }

    public void populateTableWithSampleData() throws SQLException {
        try (Connection conn = getDbConnection()) {
            try (Statement stmt = conn.createStatement()) {
                log.info("Populating TRUCK table with data");
                stmt.execute("INSERT INTO truck VALUES('EMEA-AT-431', 'Truck 43 1 AT')");
                stmt.execute("INSERT INTO truck VALUES('EMEA-AT-432', 'Truck 43 2 AT')");
                stmt.execute("INSERT INTO truck VALUES('EMEA-AT-433', 'Truck 43 3 AT')");
                stmt.execute("INSERT INTO truck VALUES('EMEA-DE-491', 'Truck 49 1 DE')");
                stmt.execute("INSERT INTO truck VALUES('EMEA-DE-492', 'Truck 49 2 DE')");
            }
        }
    }

    public void printAllData() throws SQLException {
        log.info("Query TRUCK data");
        try (Connection conn = getDbConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT truckid, truckname FROM truck")) {
                    while (rs.next()) {
                        String truckId = rs.getString(1);
                        String truckName = rs.getString(2);
                        System.out.printf("TruckId: %s TruckName: %s%n", truckId, truckName);
                    }
                }
            }
        }
    }

    public TruckInfo readTruck(String id) throws SQLException {
        log.info("Read TRUCK data for truckid=" + id);
        TruckInfo truck = null;
        try (Connection conn = getDbConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT truckid, truckname FROM truck WHERE truckid=?")) {
                stmt.setString(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String truckId = rs.getString(1);
                        String truckName = rs.getString(2);
                        System.out.printf("TruckId: %s TruckName: %s%n", truckId, truckName);

                        if (rs.next()) {
                            throw new RuntimeException("Too many results found");
                        }
                        return new TruckInfo(truckId, truckName);
                    }
                }
            }
        }
        return null;
    }

    public Collection<TruckInfo> findAllTrucks(String region, String country) throws SQLException {
        log.info("Find all TRUCKs for region=" + region + " country=" + country);

        if("AT".equals(country)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else if("DE".equals(country)) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Collection<TruckInfo> result = new ArrayList<>();
        try (Connection conn = getDbConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("SELECT truckid, truckname FROM truck WHERE truckid LIKE ?")) {
                String searchString = region + "-" + country + "%";
                stmt.setString(1, searchString);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String truckId = rs.getString(1);
                        String truckName = rs.getString(2);
                        System.out.printf("TruckId: %s TruckName: %s%n", truckId, truckName);
                        result.add(new TruckInfo(truckId, truckName));
                    }
                }
            }
        }
        return result;
    }
}
