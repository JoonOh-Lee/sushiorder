package com.joonoh.sushiorder.domain.staff.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StaffPasswordChangeRequest {
    @NotBlank
    private String password;
}
