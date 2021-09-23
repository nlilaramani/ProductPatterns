package com.ps.pm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;

@SpringBootApplication
@EnableBinding(value={Source.class})
public class ProductMaintainanceApplication {
        
	public static void main(String[] args) {
		SpringApplication.run(ProductMaintainanceApplication.class, args);
	}

}
