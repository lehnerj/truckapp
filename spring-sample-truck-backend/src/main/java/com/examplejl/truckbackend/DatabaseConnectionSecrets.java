package com.examplejl.truckbackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionSecrets {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUser;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String datasourceDriverClassName;

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    public void setDatasourceUser(String datasourceUser) {
        this.datasourceUser = datasourceUser;
    }

    public void setDatasourcePassword(String datasourcePassword) {
        this.datasourcePassword = datasourcePassword;
    }

    public void setDatasourceDriverClassName(String datasourceDriverClassName) {
        this.datasourceDriverClassName = datasourceDriverClassName;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public String getDatasourceUser() {
        return datasourceUser;
    }

    public String getDatasourcePassword() {
        return datasourcePassword;
    }

    public String getDatasourceDriverClassName() {
        return datasourceDriverClassName;
    }
}
