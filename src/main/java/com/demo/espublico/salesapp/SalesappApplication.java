package com.demo.espublico.salesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.demo.espublico.salesapp"})
@EnableJpaRepositories(basePackages="com.demo.espublico.salesapp.repositories")
@EnableTransactionManagement
@EntityScan(basePackages="com.demo.espublico.salesapp.entities")
public class SalesappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesappApplication.class, args);
    }



}
