package com.example.restservice;

import java.util.Map;

public class Greeting {
//    private final long id;
    private final String number;
    private final String phone;
//    private final String content;
//    private final String responseMsg1;
//    private final String responseMsg2;
    private final Map<String,String> greetingResult;

    public Greeting(String number, String phone, Map<String, String> greetingResult) {
        this.number = number;
        this.phone = phone;
        this.greetingResult = greetingResult;
    }

//    public Greeting(long id, int number, String phone, String content, String responseMsg1, String responseMsg2, Map<String, String> greetingResult) {
//        this.id = id;
//        this.number = number;
//        this.phone = phone;
//        this.content = content;
//        this.responseMsg1 = responseMsg1;
//        this.responseMsg2 = responseMsg2;
//        this.greetingResult = greetingResult;
//    }

//    public long getId() {
//        return id;
//    }
//
//    public String getContent() {
//        return content;
//    }

    public String getNumber() {
        return number;
    }

    public String getPhone() {
        return phone;
    }

//    public String getResponseMsg1() {
//        return responseMsg1;
//    }
//
//    public String getResponseMsg2() {
//        return responseMsg2;
//    }

    public Map<String, String> getGreetingResult() {
        return greetingResult;
    }
}
