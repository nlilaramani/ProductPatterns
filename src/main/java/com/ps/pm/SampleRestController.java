/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ps.pm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author itexps
 */
@RestController
public class SampleRestController {
    @GetMapping("/hello")
    public String sayHello(){
        return "Hello World!";
    }
}
