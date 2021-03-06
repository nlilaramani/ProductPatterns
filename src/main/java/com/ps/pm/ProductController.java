/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.pm;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author itexps
 */
@RestController
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired 
    private ProductRepo productRepo;
    @Autowired
    private ProductService service;
    @Autowired
    SimpleSourceBean simpleSourceBean;
    
    @Autowired
    private ProductServiceSample service1;
    @Autowired
    private ReactiveCircuitBreakerFactory circuitBreakerFactory;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Iterable<ProductContract> getAllProductsTest(){
        ProductContract p=new ProductContract(1,"TV","TV",500.00);
        List<ProductContract> result=new ArrayList<ProductContract>();
        result.add(p);
        return result;
    }
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public Iterable<Product> getAllProducts(){
        logger.debug("Getting all products");
        return service1.getAllProducts();        
    }

    @RequestMapping(value = "/productsRetry", method = RequestMethod.GET)
    public Iterable<Product> getAllProductsRetry(){
        //return service1.getAllProductsLimiter();
        return service1.getAllProductsWithRetry();
    }
    
    @RequestMapping(value = "/productsBreaker", method = RequestMethod.GET)
    public Iterable<Product> getAllProductsBreaker(){
        //return service1.getAllProductsLimiter();
        return service1.getAllProductsBreaker();
    }
    
    @RequestMapping(value = "/productsLimiter", method = RequestMethod.GET)
    public Iterable<Product> getAllProductsLimiter(){
        return service1.getAllProductsLimiter();
    }
    
    @RequestMapping(value = "/productsFallback", method = RequestMethod.GET)
    public Iterable<Product> getAllProductsFallback(){
        return service1.getAllProductsFallback();
    }
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ProductContract addProduct(@RequestBody ProductContract pc){
        Product p=new Product();
        p.setProductId(pc.getId());
        p.setName(pc.getName());
        p.setDescription(pc.getDescription());
        p.setPrice(pc.getPrice());
        productRepo.save(p);
        simpleSourceBean.publishProductChange("SAVE",pc);
        return pc;
    }
    
    @RequestMapping(value="/products/{id}", method=RequestMethod.DELETE)
    public void deleteProduct(@PathVariable(name="id") Integer id){
        productRepo.deleteById(id);
    }
    
    @RequestMapping(value="/products/{id}", method=RequestMethod.GET)
    public Product getProduct(@PathVariable(name="id") Integer id){
        logger.debug("Retrieving product by id "+id);
        return productRepo.findById(id).get();
    }
}
