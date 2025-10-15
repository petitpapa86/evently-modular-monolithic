package com.evently.api;

import com.evently.api.security.JwtService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public Map<String, String> generateToken(@RequestParam String identityId, @RequestParam String email) {
        // For testing purposes, generate a token with some default roles and permissions
        String token = jwtService.generateToken(identityId, email, "ROLE_USER", "users:read", "events:create");
        return Map.of("token", token);
    }

    @PostMapping("/token/custom")
    public Map<String, String> generateCustomToken(@RequestParam String identityId, @RequestParam String email, @RequestParam String[] permissions) {
        // For testing purposes, generate a token with custom permissions
        String[] rolesAndPermissions = new String[permissions.length + 1];
        rolesAndPermissions[0] = "ROLE_USER";
        System.arraycopy(permissions, 0, rolesAndPermissions, 1, permissions.length);

        String token = jwtService.generateToken(identityId, email, rolesAndPermissions);
        return Map.of("token", token);
    }
}