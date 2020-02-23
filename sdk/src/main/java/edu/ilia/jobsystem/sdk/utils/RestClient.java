package edu.ilia.jobsystem.sdk.utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.concurrent.CompletableFuture;

/**
 * @author Ilia
 * @date 17/03/2018
 */
public interface RestClient {
    <T> CompletableFuture<T> invokeServiceAsync(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method);

    <T> CompletableFuture<T> invokeServiceAsync(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method, String authorizationToken);

    <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method);

    <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method, String authorizationToken);

    <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpHeaders headers, HttpMethod method);

    <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpHeaders headers, HttpMethod method, String authorizationToken, Object... params);
    void setAuthorizationToken(String token);

    String getApiHost();
    void setApiHost(String apiHostUrl);

    HttpHeaders getHttpHeaders();
    void setHttpHeaders(HttpHeaders httpHeaders);
    void addHttpHeader(String key, String value);
}
