package com.example.apidemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("/x")
    public String x() { return "index2";}
}
