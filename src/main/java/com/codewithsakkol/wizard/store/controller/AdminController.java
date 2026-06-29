package com.codewithsakkol.wizard.store.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/hello")
    public String helloAdmin(){
        return  "Hello Admin!";
    }

}
