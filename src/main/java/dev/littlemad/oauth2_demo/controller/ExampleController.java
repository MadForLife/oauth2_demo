package dev.littlemad.oauth2_demo.controller;

import dev.littlemad.oauth2_demo.util.JwtUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('user')")
@CrossOrigin(origins = "http://localhost:8050")
public class ExampleController {

    private final JwtUtils jwtUtils;

    public ExampleController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/example")
    public List<String> exampleData(Authentication authentication) {

        String email = jwtUtils.extractEmail(authentication);
        String id = jwtUtils.extractUserId(authentication);
        return List.of("Hello", "World", email, id);
    }
}
