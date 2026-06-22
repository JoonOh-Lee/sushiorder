package com.joonoh.sushiorder.domain.staff.service;

import com.joonoh.sushiorder.domain.staff.dto.LoginRequest;
import com.joonoh.sushiorder.domain.staff.dto.LoginResponse;
import com.joonoh.sushiorder.domain.staff.entity.Staff;
import com.joonoh.sushiorder.domain.staff.exception.InvalidCredentialsException;
import com.joonoh.sushiorder.domain.staff.repository.StaffRepository;
import com.joonoh.sushiorder.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        Staff staff = staffRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), staff.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenProvider.createToken(staff.getUsername(), staff.getRole());

        return LoginResponse.builder()
                .token(token)
                .username(staff.getUsername())
                .role(staff.getRole())
                .build();
    }
}
