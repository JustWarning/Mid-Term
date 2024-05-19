package com.midterm.controller;

import com.midterm.security.JwtResponse;
import com.midterm.security.LoginRequest;
import com.midterm.security.MessageResponse;
import com.midterm.security.SignupRequest;
import com.midterm.security.jwt.JwtUtils;
import com.midterm.entity.User;
import com.midterm.entity.VerificationToken;
import com.midterm.service.framework.UserService;
import com.midterm.service.implementation.VerificationTokenService;
import com.midterm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    EmailService emailService;

    @Autowired
    VerificationTokenService verificationTokenService;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();
        // Предполагается, что userDetails имеет тип User

        return ResponseEntity.ok(new JwtResponse(jwt, ""));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

        userService.save(user);

        VerificationToken verificationToken = verificationTokenService.createVerificationToken(user);

        String confirmationUrl = "http://localhost:8080/api/auth/confirm?token=" + verificationToken.getToken();
        emailService.sendSimpleMessage(user.getEmail(), "Email Confirmation", "Click the link to confirm your email: " + confirmationUrl);

        return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for confirmation link."));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam("token") String token) {
        Optional<VerificationToken> verificationToken = verificationTokenService.findByToken(token);
        if (!verificationToken.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token!"));
        }

        VerificationToken tokenEntity = verificationToken.get();
        if (verificationTokenService.isTokenExpired(tokenEntity)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Token expired!"));
        }

        User user = tokenEntity.getUser();
        user.setEnabled(true);
        userService.save(user);
        verificationTokenService.delete(tokenEntity);

        return ResponseEntity.ok(new MessageResponse("User confirmed successfully!"));
    }
}
