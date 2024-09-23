package com.kitchensink.ks.configurations;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig {
    @Value("${mongodb.username}")
    private String username;
    @Value("${mongodb.password}")
    private String password;
    @Value("${mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${mongodb.connection.timeout}")
    private int connectionTimeout;
    @Value("${mongodb.read.timeout}")
    private int readTimeout;

    @Bean
    public MongoClientSettings mongoClientSettings() {
        String mongoUri = String.format("mongodb+srv://%s:%s@%s/%s", username, password, host, database);
        ConnectionString connectionString = new ConnectionString(mongoUri);
        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSocketSettings(builder -> builder
                        .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                        .readTimeout(readTimeout, TimeUnit.MILLISECONDS))
                .build();
    }
}
