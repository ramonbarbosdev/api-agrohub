package com.api_agrohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.api_agrohub.config.DotenvLoader;

@SpringBootApplication
@ComponentScan(basePackages = { "com.*" })
@EntityScan(basePackages = { "com.api_agrohub.domain.*" })
@EnableJpaRepositories(basePackages = { "com.api_agrohub.domain.*" })
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
@EnableScheduling
@EnableMethodSecurity
public class ApiAgrohubApplication {

	public static void main(String[] args) {

		DotenvLoader.init();
		SpringApplication.run(ApiAgrohubApplication.class, args);
	}

}
