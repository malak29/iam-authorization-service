package com.iam.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.iam.authorization",
        "com.iam.common"
})
@EnableR2dbcRepositories(basePackages = {"com.iam.authorization.repository", "com.iam.common.repository"})
public class AuthorizationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServiceApplication.class, args);
    }
}