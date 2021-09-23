/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.pm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 *
 * @author itexps
 */
@Component
public class SimpleSourceBean {
    private Source source;
    @Autowired
    public SimpleSourceBean(Source source){
        this.source=source;
    }
    public void publishProductChange(String action,ProductContract pc){
        System.out.println("Sending message to kafka");
        source.output().send(MessageBuilder.withPayload(pc).build());
        
    }
    
}
