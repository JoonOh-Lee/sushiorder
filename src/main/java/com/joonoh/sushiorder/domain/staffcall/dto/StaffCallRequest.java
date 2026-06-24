package com.joonoh.sushiorder.domain.staffcall.dto;

import com.joonoh.sushiorder.domain.staffcall.entity.CallType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StaffCallRequest {

    @NotNull
    private CallType type;

    /** type이 ITEM_REQUEST일 때만 필수 — 요청할 물품명 */
    @Size(max = 50)
    private String itemName;
}
