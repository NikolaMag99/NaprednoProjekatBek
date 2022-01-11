package raf.rs.NwpNikolaDomaci3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import raf.rs.NwpNikolaDomaci3.requests.LoginRequest;
import raf.rs.NwpNikolaDomaci3.responses.LoginResponse;
import raf.rs.NwpNikolaDomaci3.services.UserService;
import raf.rs.NwpNikolaDomaci3.utils.JwtUtil;

import java.util.Collection;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;


    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            System.out.println("OK!");
            System.out.println(userService.findByEmail(loginRequest.getUsername()).getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getUsername()), userService.findByEmail(loginRequest.getUsername()).getId()));
    }
}
