package com.innowise.enricherapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Value("${spring.cloud.sqs.endpoint}")
    private String sqsEndpoint;
    @Value("${spring.cloud.region.static}")
    private String region;
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create(sqsEndpoint))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(region))
                .build();
    }


}
