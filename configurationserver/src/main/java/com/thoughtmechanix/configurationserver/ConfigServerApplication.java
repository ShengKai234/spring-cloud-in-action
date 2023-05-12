package com.thoughtmechanix.configurationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.config.server.config.EncryptionAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigServer
@Import(EncryptionAutoConfiguration.class)
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
