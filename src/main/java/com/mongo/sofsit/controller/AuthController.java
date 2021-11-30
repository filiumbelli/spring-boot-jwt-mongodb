package com.mongo.sofsit.controller;
import com.mongo.sofsit.config.security.jwt.JwtUtil;
import com.mongo.sofsit.model.ResponseMessage;
import com.mongo.sofsit.model.User;
import com.mongo.sofsit.model.dto.JwtRequest;
import com.mongo.sofsit.model.dto.JwtResponse;
import com.mongo.sofsit.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authorization")
public class AuthController {
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @Autowired
    public AuthController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> login(@RequestBody JwtRequest dto) {
        ResponseMessage authenticate = authenticate(dto.getUsername(), dto.getPassword());
        if(authenticate == null) {
            UserDetails userDetails = userService.findByUsername(dto.getUsername());
            final String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok().body(new ResponseMessage(token,200));
        }
        return ResponseEntity.badRequest().body(authenticate);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> register(@RequestBody JwtRequest dto) {
        if(userService.findByUsername(dto.getUsername())!= null) return ResponseEntity.ok().body(new JwtResponse("User is already taken"));
        User user = new User(dto.getUsername(), dto.getPassword());
        userService.saveUser(user);
        final String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok().body(new JwtResponse(token));

    }

    private ResponseMessage authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return null;
        } catch (DisabledException ex) {
            return new ResponseMessage("Disabled user", 401);
        } catch (BadCredentialsException ex) {
            return new ResponseMessage("Bad Credentials", 401);
        }
    }
}
