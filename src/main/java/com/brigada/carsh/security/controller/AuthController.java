package com.brigada.carsh.security.controller;


import com.brigada.carsh.dto.request.LoginRequest;
import com.brigada.carsh.dto.request.RegisterRequest;
import com.brigada.carsh.dto.response.JwtResponse;
import com.brigada.carsh.security.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtResponse register(@RequestBody @Valid RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.login(request);
    }
}
