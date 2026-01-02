package com.tanyde.service.admin;


import com.tanyde.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        System.out.println("Hello!");
        return "hello!";
    }
}
