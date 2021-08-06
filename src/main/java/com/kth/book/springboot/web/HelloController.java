package com.kth.book.springboot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
    @GetMapping("/hello-dto")
    public String helloDto(@RequestParam String name,
                           @RequestParam int amount) {
        log.info("name = {}",name);
        log.info("amount = {}",amount);
        return "hello";
    }
}
