package com.job_graph_app.config;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public Driver neo4jDriver() {

        String uri = System.getenv("COGNODB_URI");
        String username = System.getenv("COGNODB_USERNAME");
        String password = System.getenv("COGNODB_PASSWORD");

        if (uri == null || username == null || password == null) {
            throw new IllegalStateException(
                "CognoDB environment variables are not configured."
            );
        }

        return GraphDatabase.driver(
            uri,
            AuthTokens.basic(username, password)
        );
    }
}