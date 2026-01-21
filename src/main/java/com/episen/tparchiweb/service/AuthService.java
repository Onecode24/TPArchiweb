package com.episen.tparchiweb.service;

import com.episen.tparchiweb.dto.LoginResponse;
import com.episen.tparchiweb.dto.RegisterRequest;
import com.episen.tparchiweb.dto.UserDTO;
import com.episen.tparchiweb.exception.AuthenticationException;
import com.episen.tparchiweb.exception.RegistrationException;
import com.episen.tparchiweb.model.User;
import com.episen.tparchiweb.repository.UserRepository;
import com.episen.tparchiweb.security.util.JwtUtil;
import com.episen.tparchiweb.security.util.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordUtil passwordUtil;

    @Inject
    private JwtUtil jwtUtil;

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new AuthenticationException();
        }

        if (!passwordUtil.verifyPassword(password, user.getPassword())) {
            throw new AuthenticationException();
        }

        String token = jwtUtil.generateToken(user);
        UserDTO userDTO = new UserDTO(user);

        return new LoginResponse(token, userDTO);
    }

    public UserDTO register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new RegistrationException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordUtil.hashPassword(request.getPassword()));
        user.setIsAdmin(false);

        user = userRepository.save(user);
        return new UserDTO(user);
    }
}
