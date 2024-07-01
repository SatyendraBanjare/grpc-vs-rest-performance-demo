package com.satyendra.rest_server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 

@RestController
public class Controller {

    @GetMapping(path = "/{num}")
    public int getSquare(@PathVariable int num){
        // System.out.println(num);
        return num*num;
    }
}
