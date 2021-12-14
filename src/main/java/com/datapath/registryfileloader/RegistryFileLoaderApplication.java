package com.datapath.registryfileloader;

import com.datapath.registryfileloader.service.FileLoadService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AllArgsConstructor
@SpringBootApplication
public class RegistryFileLoaderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RegistryFileLoaderApplication.class, args);
    }

    private final FileLoadService service;

    @Override
    public void run(String... args) {
        service.load();
    }
}
