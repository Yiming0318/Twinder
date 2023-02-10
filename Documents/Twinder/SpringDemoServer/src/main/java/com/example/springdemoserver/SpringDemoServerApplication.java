package com.example.springdemoserver;

import entities.ResponseMessage;
import entities.SwipeDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringDemoServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringDemoServerApplication.class, args);
  }


  @GetMapping("swipe/hello")
  public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
    return String.format("Hello %s!", name);
  }


}


