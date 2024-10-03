package com.examplejl.truckfrontend;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;

@Service
@RestController
@RequestMapping("/truckfrontend")
public class TruckFrontend {

    private static final Logger log = LoggerFactory.getLogger(TruckFrontend.class);

    private RestClient restClient;

    @Value("${truck.targetEndpoint}")
    private String targetEndpoint;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @PostConstruct
    public void postConstruct() {
        this.restClient = restClientBuilder.baseUrl("http://" + targetEndpoint + "/truckrest").build();
    }

    @RequestMapping("truck/{id}")
    public String getTruck(@PathVariable("id") String truckId,
                           @RequestParam(value = "stallSec", required = false) Integer stallSec,
                           @RequestParam(value = "stallOp", required = false) String stallOp,
                           @RequestParam(value = "fibonacci", required = false) Integer fibonacci) {
        stallRequest(stallSec, stallOp, fibonacci);
        return this.restClient.get().uri("/truck/{id}", truckId).retrieve().body(String.class);
    }

    @RequestMapping("fibonacci")
    public ResponseEntity<String> fibonacci(@RequestParam(value = "fibonacci", required = false) Integer fibonacci
    ) {
        String text = calcFib(fibonacci);
        return ResponseEntity.ok(text);
    }

    @RequestMapping("/trucks/{region}/{country}")
    public String getAllTrucksFromRegion(@PathVariable("region") String region,
                                         @PathVariable("country") String country,
                                         @RequestParam(value = "stallSec", required = false) Integer stallSec,
                                         @RequestParam(value = "stallOp", required = false) String stallOp,
                                         @RequestParam(value = "fibonacci", required = false) Integer fibonacci) {
        stallRequest(stallSec, stallOp, fibonacci);
        return this.restClient.get().uri("/trucks/{region}/{country}", region, country).retrieve().body(String.class);
    }

    @RequestMapping("/health")
    public String health(@RequestParam(value = "stallSec", required = false) Integer stallSec,
                         @RequestParam(value = "stallOp", required = false) String stallOp,
                         @RequestParam(value = "fibonacci", required = false) Integer fibonacci) {
        stallRequest(stallSec, stallOp, fibonacci);
        return this.restClient.get().uri("/health").retrieve().body(String.class);
    }

    @RequestMapping("/release")
    public String release(@RequestParam(value = "stallSec", required = false) Integer stallSec,
                          @RequestParam(value = "stallOp", required = false) String stallOp,
                          @RequestParam(value = "fibonacci", required = false) Integer fibonacci) {
        stallRequest(stallSec, stallOp, fibonacci);
        return this.restClient.get().uri("/release").retrieve().body(String.class);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(@RequestBody String request,
                                          @RequestParam(value = "stallSec", required = false) Integer stallSec,
                                          @RequestParam(value = "stallOp", required = false) String stallOp,
                                          @RequestParam(value = "fibonacci", required = false) Integer fibonacci) {
        stallRequest(stallSec, stallOp, fibonacci);
        log.warn("webhook: " + request); // warn log level to be picked up by APM:
        return ResponseEntity.ok().body(request);
    }

    private static void stallRequest(Integer stallSec, String stallOp, Integer fibonacci) {
        if(stallSec != null && stallSec > 0) {
            if ("cpu".equals(stallOp)) {
                Instant before = Instant.now();
                Instant after;
                do {
                    String text = calcFib(fibonacci != null ? fibonacci : 40);// Fib(n=40) takes around 250ms on my machine
                    log.info(text);
                    after = Instant.now();
                } while (Duration.between(before, after).toSeconds() < stallSec);
            } else {
                try {
                    Thread.sleep(stallSec*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static String calcFib(Integer n) {
        Instant before = Instant.now();
        int fibonacci = fibonacciRecursive(n);
        Instant after = Instant.now();
        long delta = Duration.between(before, after).toMillis();
        String text = "Stall Request: Elapsed Time to calculate fibonacci(" + n + " -> " + fibonacci + "): " + delta + "ms";
        return text;
    }

    public static int fibonacciRecursive(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
        }
    }
}