package com.example.shopbackend.demo.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shopbackend.demo.common.BadCredentialsException;
import com.example.shopbackend.demo.common.EmailAlreadyInUseException;
import com.example.shopbackend.demo.user.Role;
import com.example.shopbackend.demo.user.User;
import com.example.shopbackend.demo.user.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email()))
            throw new EmailAlreadyInUseException(req.email());
        User user = new User(
                req.email(),
                passwordEncoder.encode(req.password()),
                Role.USER);

        userRepository.save(user);
    }

    public String login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new BadCredentialsException());

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash()))
            throw new BadCredentialsException();

        return jwtService.generateToken(user.getEmail());
    }

}
