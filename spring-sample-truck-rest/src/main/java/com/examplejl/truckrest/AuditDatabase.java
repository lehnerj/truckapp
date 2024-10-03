package com.examplejl.truckrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class AuditDatabase {

    private static final Logger log = LoggerFactory.getLogger(AuditDatabase.class);
    private final DatabaseConnectionSecrets dbSecrets;

    public AuditDatabase(DatabaseConnectionSecrets dbSecrets) {
        this.dbSecrets = dbSecrets;
    }

    private Connection getDbConnection() throws SQLException {
        return DriverManager.getConnection(dbSecrets.getDatasourceUrl() +
                "?user=" + dbSecrets.getDatasourceUser() + "&password=" + dbSecrets.getDatasourcePassword());
    }

    public void createTable() throws SQLException {
        try (Connection conn = getDbConnection()) {
            try (Statement stmt = conn.createStatement()) {
                log.info("Creating Audit table");
                stmt.execute("DROP TABLE IF EXISTS audit");
                stmt.execute("CREATE TABLE audit(auditid VARCHAR(255), msg VARCHAR(255))");
            }
        }
    }

    public void audit(String msg) throws SQLException {
        try (Connection conn = getDbConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO audit VALUES(?, ?)")) {
                String dateYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
                String uuid = UUID.randomUUID().toString();
                String auditId = "AUDIT-" + dateYYYYMMDD + uuid;
                log.info("audit: " + auditId + " msg:" + msg);
                stmt.setString(1, auditId);
                stmt.setString(2, msg);
                stmt.execute();
            }
        }
    }

}
