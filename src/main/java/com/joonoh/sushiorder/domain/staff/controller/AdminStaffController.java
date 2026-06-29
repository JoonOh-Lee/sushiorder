package com.joonoh.sushiorder.domain.staff.controller;

import com.joonoh.sushiorder.domain.staff.dto.*;
import com.joonoh.sushiorder.domain.staff.service.StaffService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
public class AdminStaffController {

    private final StaffService staffService;

    @GetMapping
    public ApiResponse<List<StaffResponse>> getAllStaff() {
        return ApiResponse.success(staffService.getAllStaff());
    }

    @PostMapping
    public ApiResponse<StaffResponse> createStaff(@Valid @RequestBody StaffCreateRequest request) {
        return ApiResponse.success(staffService.createStaff(request));
    }

    @PatchMapping("/{id}/role")
    public ApiResponse<Void> changeRole(
            @PathVariable Long id,
            @Valid @RequestBody StaffRoleChangeRequest request) {
        staffService.changeRole(id, request.getRole());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/password")
    public ApiResponse<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody StaffPasswordChangeRequest request) {
        staffService.changePassword(id, request.getPassword());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivate(@PathVariable Long id, Authentication authentication) {
        staffService.deactivate(id, authentication.getName());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/activate")
    public ApiResponse<Void> activate(@PathVariable Long id) {
        staffService.activate(id);
        return ApiResponse.success();
    }
}
