package com.theojamilly.gcm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.theojamilly.gcm.service.LojaService;

@RestController 
@RequestMapping("/loja")
public class LojaController {
    private final LojaService lojaService;

    public LojaController(LojaService lojaService) {
        this.lojaService = lojaService;
    }
}
