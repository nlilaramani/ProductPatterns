/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.pm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 *
 * @author itexps
 */
@Service
@Component
public class ProductService {
     @Autowired 
    private ProductRepo productRepo;
    private final ReactiveCircuitBreaker readingListCircuitBreaker;
    public ProductService(ReactiveCircuitBreakerFactory circuitBreakerFactory){
        this.readingListCircuitBreaker = circuitBreakerFactory.create("recommended");
        
    }
     public Mono<Iterable<Product>> getAllProducts(){
         return readingListCircuitBreaker.run(Mono.just(productRepo.findAll()),(a) -> {
             return null; //To change body of generated lambdas, choose Tools | Templates.
         });
    }
     
     public Mono<Iterable<Product>> getProductsMono(){
         return Mono.just(productRepo.findAll());
     }
}
