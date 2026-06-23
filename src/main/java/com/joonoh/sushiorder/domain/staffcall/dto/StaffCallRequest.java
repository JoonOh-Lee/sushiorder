package com.joonoh.sushiorder.domain.staffcall.dto;

import com.joonoh.sushiorder.domain.staffcall.entity.CallType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StaffCallRequest {

    @NotNull
    private CallType type;
}
