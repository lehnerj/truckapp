package com.examplejl.truckfrontend;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@EnableScheduling
public class TruckFrontendMetrics {
    private static final Logger log = LoggerFactory.getLogger(TruckFrontendMetrics.class);

    private final AtomicInteger truckSecondOfMinute = new AtomicInteger();
    private final AtomicInteger truckSecondOfMinuteEquals30 = new AtomicInteger();
    private final AtomicInteger truckEverySecondMinuteSecondEquals30 = new AtomicInteger();
    private final AtomicInteger truckEveryFiveMinutesSecondEquals30 = new AtomicInteger();

    public TruckFrontendMetrics(MeterRegistry registry) {
        Gauge.builder("truck.secondofminute.gauge", truckSecondOfMinute, AtomicInteger::get)
                .description("Gauges something")
                .tags("region", "us-east")
                .register(registry);

        Gauge.builder("truck.ifsecondequals30.gauge", truckSecondOfMinuteEquals30, AtomicInteger::get)
                .description("Gauges something")
                .tags("region", "us-east")
                .register(registry);

        Gauge.builder("truck.everysecondminutesecondequals30.gauge", truckEverySecondMinuteSecondEquals30, AtomicInteger::get)
                .description("Gauges something")
                .tags("region", "us-east")
                .register(registry);

        Gauge.builder("truck.everyfiveminutessecondequals30.gauge", truckEveryFiveMinutesSecondEquals30, AtomicInteger::get)
                .description("Gauges something")
                .tags("region", "us-east")
                .register(registry);
    }

    @Scheduled(cron = "* * * ? * *")
    public void setGauge() {
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int second = now.getSecond();
        log.info("Setting metric truck.secondofminute.gauge = " + second);
        truckSecondOfMinute.set(second);

        truckSecondOfMinuteEquals30.set(0);
        if(second==30) {
            log.info("Setting metric truck.ifsecondequals30.gauge = " + second);
            truckSecondOfMinuteEquals30.set(second);
        }

        truckEverySecondMinuteSecondEquals30.set(0);
        truckEveryFiveMinutesSecondEquals30.set(0);
        if(second == 30 && minute % 2 == 0) {
            log.info("Setting metric truck.everysecondminutesecondequals30.gauge = " + second);
            truckEverySecondMinuteSecondEquals30.set(second);
        }
        if(second == 30 && minute % 5 == 0) {
            log.info("Setting metric truck.everyfiveminutessecondequals30.gauge = " + second);
            truckEveryFiveMinutesSecondEquals30.set(second);
        }
    }
}
