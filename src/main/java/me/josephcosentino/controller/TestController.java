package me.josephcosentino.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joseph Cosentino.
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping
    public String ping() {
        return "Hello World!";
    }

}
