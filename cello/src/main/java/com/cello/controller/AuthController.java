package com.cello.controller;

import com.cello.entity.Role;
import com.cello.entity.User;
import com.cello.payload.JWTAuthResponse;
import com.cello.payload.LoginDto;
import com.cello.payload.SignUpDto;
import com.cello.repository.RoleRepository;
import com.cello.repository.UserRepository;
import com.cello.security.JwtTokenProvider;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
   @Autowired
    private RoleRepository roleRepo;


    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            new ResponseEntity<>("email already exists-" + signUpDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            new ResponseEntity<>("username is exists-"+signUpDto.getUsername(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles =roleRepo.findByName("ROLE_ADMIN").get();


        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
         return new ResponseEntity<>("user is now registered!!",HttpStatus.CREATED);



    }
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return   ResponseEntity.ok(new JWTAuthResponse(token));



    }
}
