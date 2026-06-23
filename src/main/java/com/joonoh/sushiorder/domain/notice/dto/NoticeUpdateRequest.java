package com.joonoh.sushiorder.domain.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeUpdateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
