package com.joonoh.sushiorder.domain.station.controller;

import com.joonoh.sushiorder.domain.station.dto.StationResponse;
import com.joonoh.sushiorder.domain.station.service.StationService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping
    public ApiResponse<List<StationResponse>> getActiveStations() {
        return ApiResponse.success(stationService.getActiveStations());
    }
}
