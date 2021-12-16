package com.datapath.registryfileloader;

import com.datapath.registryfileloader.service.FileLoadService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@AllArgsConstructor
@SpringBootApplication
public class RegistryFileLoaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryFileLoaderApplication.class, args);
    }

    private final FileLoadService service;

    //    @Scheduled()
    private void load() {
        service.load();
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    private void update() {
        service.update();
    }
}
