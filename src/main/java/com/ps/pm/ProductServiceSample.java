/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.pm;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author itexps
 */

@Service
public class ProductServiceSample {
     @Autowired 
     private ProductRepo productRepo;
     private final CircuitBreaker circuitBreaker;
     private final RateLimiter rateLimiter;
     private final Retry retry;
     
     public Iterable<Product> getAllProducts(){
         return productRepo.findAll();
     }
     
     public Iterable<Product> getAllProductsBreaker(){
        System.out.println(circuitBreaker+" "+circuitBreaker.getState());
        Supplier<Iterable<Product>> decorated = CircuitBreaker
            .decorateSupplier(circuitBreaker, productRepo::findAll);
        return decorated.get();
     }
     
     public Iterable<Product> getAllProductsLimiter(){
        System.out.println("Settingup with Limiter");
        Supplier<Iterable<Product>> decorated = RateLimiter
            .decorateSupplier(rateLimiter, productRepo::findAll);
        return decorated.get();
     }
     
     public Iterable<Product> getAllProductsWithRetry(){
         System.out.println("Settingup with Retries");
        Supplier<Iterable<Product>> decorated = Retry
            .decorateSupplier(retry, productRepo::findAll);
        return decorated.get();
     }
     
     
    public ProductServiceSample(){
         CircuitBreakerConfig config = CircuitBreakerConfig.custom()
        .failureRateThreshold(2)
        .ringBufferSizeInClosedState(3)
         .permittedNumberOfCallsInHalfOpenState(3)
         .automaticTransitionFromOpenToHalfOpenEnabled(true)
         .enableAutomaticTransitionFromOpenToHalfOpen()
          .waitDurationInOpenState(Duration.ofSeconds(20))
        .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        circuitBreaker = registry.circuitBreaker("PS");
        
        RateLimiterConfig rconfig = RateLimiterConfig.custom()
                .limitForPeriod(2)
                .limitRefreshPeriod(Duration.ofSeconds(60))
                .build();
        RateLimiterRegistry rregistry = RateLimiterRegistry.of(rconfig);
        rateLimiter = rregistry.rateLimiter("PS");
        
        RetryConfig retryConfig = RetryConfig.custom().maxAttempts(2).build();
        RetryRegistry retryRegistry = RetryRegistry.of(retryConfig);
        retry = retryRegistry.retry("PS Retry");
     }
    
}
