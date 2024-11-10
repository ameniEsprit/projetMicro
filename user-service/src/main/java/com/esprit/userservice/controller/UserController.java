package com.esprit.userservice.controller;

import com.esprit.userservice.dto.AuthenticationRequest;
import com.esprit.userservice.dto.RegisterRequest;
import com.esprit.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

   private final UserService userService ;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        String response = userService.login(authenticationRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user-only")
    @PreAuthorize("hasRole('user')")
    public String getUserData() {
        return "This is user data.";
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('admin')")
    public String getAdminData() {
        return "This is admin data.";
    }




    @GetMapping("/current-user-connected")
    public ResponseEntity<String> currentUserConnected(@AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok("response of testing current user auth "+jwt.getSubject());
    }
    @PostMapping("/singup")
    public ResponseEntity<?> signup(  @ModelAttribute @Valid RegisterRequest userDto){

        return new ResponseEntity<>(userService.register(userDto), HttpStatus.CREATED);
    }
    @PostMapping("/addUser")
    public ResponseEntity<?> addOwner(  @ModelAttribute @Valid RegisterRequest userDto){

        return new ResponseEntity<>(userService.createOwner(userDto), HttpStatus.CREATED);
    }


}
