package com.pizza;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloWorldController
{
    @GetMapping("/hello")
    public String sayHello() {
        Map<String, String> envMap = System.getenv();
        String s = "";
        for (String envName : envMap.keySet()) {
            //System.out.format("%s = %s%n", envName, envMap.get(envName));
            s+= envName + "--" +envMap.get(envName) + "<br>";
        }
        return "Hello, World!" + "\n"+s;
    }
}


