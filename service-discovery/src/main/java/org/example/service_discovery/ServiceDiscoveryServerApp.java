package org.example.service_discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryServerApp 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServiceDiscoveryServerApp.class, args);
    }
}
