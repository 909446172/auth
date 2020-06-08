package com.example.authresource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResouceController {



    @RequestMapping("first")
    public String first() {
        return "first";
    }

    @RequestMapping("second")
    public String second() {
        return "second";
    }

    @RequestMapping("third")
    public String third() {
        return "third";
    }

    @RequestMapping("fourth")
    public String fourth() {
        return "fourth";
    }

    @RequestMapping("fifth")
    public String fifth() {
        return "fifth";
    }
    @RequestMapping("sixth")
    public String sixth() {
        return "sixth";
    }



}
