package ru.erma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.erma.customenableannotation.aop.annotation.EnableCustomLogger;


@SpringBootApplication
@EnableCustomLogger
public class MonitoringServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitoringServiceApplication.class, args);
    }
}
