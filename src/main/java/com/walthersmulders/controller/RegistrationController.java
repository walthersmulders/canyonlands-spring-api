package com.walthersmulders.controller;

import com.walthersmulders.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/register")
@RestController
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {this.registrationService = registrationService;}

    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping()
    public void register(@AuthenticationPrincipal Jwt jwt) {
        registrationService.register(jwt);
    }
}
