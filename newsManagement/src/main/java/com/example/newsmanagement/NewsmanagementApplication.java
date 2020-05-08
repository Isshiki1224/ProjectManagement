package com.example.newsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NewsmanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsmanagementApplication.class, args);
    }

}
