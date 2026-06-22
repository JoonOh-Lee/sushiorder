package com.joonoh.sushiorder.domain.staff.controller;

import com.joonoh.sushiorder.domain.staff.dto.LoginRequest;
import com.joonoh.sushiorder.domain.staff.dto.LoginResponse;
import com.joonoh.sushiorder.domain.staff.service.StaffService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StaffService staffService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(staffService.login(request));
    }
}
