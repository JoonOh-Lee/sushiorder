package com.joonoh.sushiorder.domain.staff.dto;

import com.joonoh.sushiorder.domain.staff.entity.StaffRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StaffRoleChangeRequest {
    @NotNull
    private StaffRole role;
}
