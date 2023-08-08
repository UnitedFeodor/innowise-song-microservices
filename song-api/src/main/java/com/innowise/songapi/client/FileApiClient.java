package com.innowise.songapi.client;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileApiClient {

    private final RestTemplate restTemplate;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final RetryRegistry retryRegistry;
    @Value("${api-gateway.uri}")
    private String apiGatewayUri;
    public void deleteFile(Integer songId) {
        String jwtBearerHeader = getJwtBearerHeaderFromContext();

        String fileApiDeleteUri = String.format("%s/file-api/files/%d", apiGatewayUri, songId);

        HttpEntity<?> httpEntity = getHttpEntityWithAuthHeader(jwtBearerHeader);

        Retry retry = retryRegistry.retry("file-api-delete");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("file-api-delete");

        retry.executeRunnable(
                () ->
                {
                    log.info("Retry executing runnable");
                    callFileApiDeleteWithCircuitBreakerRetry(circuitBreaker, fileApiDeleteUri, httpEntity);
                }
        );


    }

    private void callFileApiDeleteWithCircuitBreakerRetry(CircuitBreaker circuitBreaker, String fileApiDeleteUri, HttpEntity<?> httpEntity) {
        circuitBreaker.run(
                () -> {
                    log.info("Calling file-api...");
                    return restTemplate.exchange(fileApiDeleteUri, HttpMethod.DELETE, httpEntity, Object.class);
                },
                throwable -> {
                    log.error("File api deletion error. The service is down/working too slowly or the file may have already been deleted");
                    throw new IllegalStateException("File api is not working as expected");
                }
        );
    }

    private HttpEntity<?> getHttpEntityWithAuthHeader(String jwtBearerHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, jwtBearerHeader);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        return httpEntity;
    }

    private String getJwtBearerHeaderFromContext() {
        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String jwt = authentication.getToken().getTokenValue();
        String jwtBearerHeader = String.format("Bearer %s", jwt);
        return jwtBearerHeader;
    }
}
