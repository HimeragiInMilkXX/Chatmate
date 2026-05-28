package com.easyprofile.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class EasyProfileDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(EasyProfileDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EasyProfileDemoApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("EasyProfile demo app is ready (devtools restart cycle completed).");
    }
}
