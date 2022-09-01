package com.enmotech.kafkatest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({KafkaProperties.class})
@Slf4j
public class KafkaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaTestApplication.class, args);
        log.info("Kafka-connector-jdbc-test启动成功！");
    }

}
