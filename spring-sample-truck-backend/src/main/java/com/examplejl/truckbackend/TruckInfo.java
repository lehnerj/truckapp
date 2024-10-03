package com.examplejl.truckbackend;

public class TruckInfo {

    private final String truckId;
    private String truckName;

    public TruckInfo(String truckId) {
        this.truckId = truckId;
    }

    public TruckInfo(String truckId, String truckName) {
        this.truckId = truckId;
        this.truckName = truckName;
    }

    public String getTruckId() {
        return truckId;
    }

    public String getTruckName() {
        return truckName;
    }
}