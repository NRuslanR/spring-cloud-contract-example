package org.example.consumer_http_rest.application;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.httpclient.ApacheHttpClient;
import feign.okhttp.OkHttpClient;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConfigurationProperties(prefix = "feign.client.config.default.credentials")
public class FeignConfig 
{
    @Value("${feign.client.config.default.credentials.user-id}")
    private String userId;

    @Value("${feign.client.config.default.credentials.password}")
    private String password;
     
    /* 
    @Bean
    public OkHttpClient client()
    {
        return new OkHttpClient();
    }*/

    @Bean
    @ConditionalOnProperty(
        value = "feign.client.config.default.interceptor",
        havingValue = "custom",
        matchIfMissing = true
    )
    public RequestInterceptor customRequestInterceptor()
    {
        return requestTemplate -> {

            requestTemplate.header("user", userId);
            requestTemplate.header("password", password);
            requestTemplate.header("Accept", ContentType.APPLICATION_JSON.getMimeType());
        };
    }

    @Bean
    @ConditionalOnProperty(
        value = "feign.client.config.default.interceptor",
        havingValue = "basic-auth",
        matchIfMissing = false
    )
    public RequestInterceptor basicAuthRequestInterceptor()
    {
        return new BasicAuthRequestInterceptor(userId, password);
    }

    @Bean
    public Logger.Level feignLoggerLevel()
    {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder()
    {
        return new CustomErrorDecoder();
    }
}
