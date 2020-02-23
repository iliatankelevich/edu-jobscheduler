package edu.ilia.jobsystem.sdk.utils;

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ilia
 * @date 17/03/2018
 */
@SuppressWarnings("DefaultFileTemplate")
public class RestClientImpl implements RestClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Getter
    @Setter
    private String apiHost;

    private String authorizationToken = null;

    @Getter
    @Setter
    private HttpHeaders httpHeaders;

    private RestTemplate getRestTemplate(String fullUrl){
        RestTemplate restTemplate;
        if(fullUrl.toLowerCase().startsWith("https")){
            CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
            HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            restTemplate = new RestTemplate(requestFactory);
        }
        else {
            restTemplate = new RestTemplate();
        }

        restTemplate.getMessageConverters()
            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return restTemplate;
    }

    @Override
    public <T> CompletableFuture<T> invokeServiceAsync(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method) {
        return CompletableFuture.supplyAsync(() -> invokeService(typeRef, url, payload, new HttpHeaders(), method, null));
    }

    @Override
    public <T> CompletableFuture<T> invokeServiceAsync(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method, String authorizationToken) {
        return CompletableFuture.supplyAsync(() -> invokeService(typeRef, url, payload, new HttpHeaders(), method, authorizationToken));
    }

    @Override
    public <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method) {
        return invokeService(typeRef, url, payload, new HttpHeaders(), method, null);
    }

    @Override
    public <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpMethod method, String authorizationToken) {
        return invokeService(typeRef, url, payload, new HttpHeaders(), method, authorizationToken);
    }

    public <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpHeaders headers, HttpMethod method) {
        return invokeService(typeRef, url, payload, headers, method, null); //104
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> T invokeService(ParameterizedTypeReference<T> typeRef, String url, Object payload, HttpHeaders headers, HttpMethod method, String authorizationToken, Object... params) {
        HttpEntity httpEntity;
        HttpHeaders allHeaders = getAuthorizationHeaders(authorizationToken);
        headers.forEach((headerName, headerValues) -> allHeaders.add(headerName, StringUtils.join(headerValues, ",")));

        if (payload != null) {
            httpEntity = new HttpEntity(payload, allHeaders);
        } else {
            httpEntity = new HttpEntity(allHeaders);
        }

        String fullUrl = apiHost + url;
        if(httpEntity.toString().length() <= 1000){
            log.info("request to " + fullUrl + " ~ " + httpEntity);
        }else {
            log.info("request to " + fullUrl + " long payload (switch logging to 'trace' to see it)");
            log.trace("request to " + fullUrl + " ~ " + httpEntity);
        }

        ResponseEntity<T> response;
        if (params == null || params.length == 0) {
            response = getRestTemplate(fullUrl).exchange(fullUrl, method, httpEntity, typeRef);
        } else {
            response = getRestTemplate(fullUrl).exchange(fullUrl, method, httpEntity, typeRef, params);
        }

        log.info("response code: " + response.getStatusCode());
        log.trace("response: " + response.toString());
        return response.getBody();
    }

    @Override
    public void setAuthorizationToken(String token) {
        this.authorizationToken = token;
    }

    @Override
    public void addHttpHeader(String key, String value) {
        if(httpHeaders == null){
            httpHeaders = new HttpHeaders();
        }

        httpHeaders.add(key, value);
    }

    private HttpHeaders getAuthorizationHeaders(String specificAuthorizationToken){
        HttpHeaders result = new HttpHeaders();
        if(httpHeaders == null){
            httpHeaders = new HttpHeaders();
        }

        httpHeaders.forEach((key, values) -> result.add(key, StringUtils.join(values, ",")));
        result.setContentType(MediaType.APPLICATION_JSON);

        if(specificAuthorizationToken == null){
            if(authorizationToken != null && authorizationToken.length() > 0 && httpHeaders.get("Authorization") == null){
                result.add("Authorization", authorizationToken);
            }
        } else {
            result.add("Authorization", specificAuthorizationToken);
        }


        return result;
    }
}
