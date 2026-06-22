package com.joonoh.sushiorder.domain.session.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateSessionRequest {

    @NotNull
    private Long tableId;
}
