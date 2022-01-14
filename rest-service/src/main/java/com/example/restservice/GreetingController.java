package com.example.restservice;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    GreetingService greetingService;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @PostMapping("/greeting")
    public Map<String, String> greeting(@RequestParam(value = "number", defaultValue = "1") String number, @RequestParam(value="phone") String phone) {
        Map<String, String> greetServiceResult = greetingService.prepareSendMsg(number, phone);
        return greetServiceResult;


    }
}