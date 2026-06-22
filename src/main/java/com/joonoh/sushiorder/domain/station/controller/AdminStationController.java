package com.joonoh.sushiorder.domain.station.controller;

import com.joonoh.sushiorder.domain.station.dto.StationCreateRequest;
import com.joonoh.sushiorder.domain.station.dto.StationRenameRequest;
import com.joonoh.sushiorder.domain.station.dto.StationReorderRequest;
import com.joonoh.sushiorder.domain.station.dto.StationResponse;
import com.joonoh.sushiorder.domain.station.service.StationService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/station")
@RequiredArgsConstructor
public class AdminStationController {

    private final StationService stationService;

    @GetMapping
    public ApiResponse<List<StationResponse>> getAllStation() {
        return ApiResponse.success(stationService.getAllStations());
    }

    @PostMapping
    public ApiResponse<StationResponse> createStation(@Valid @RequestBody StationCreateRequest request) {
        return ApiResponse.success(stationService.createStation(request));
    }

    @PatchMapping("/{id}/name")
    public ApiResponse<Void> renameStation(@PathVariable Long id,
                                           @Valid @RequestBody StationRenameRequest request) {
        stationService.renameStation(id, request);
        return ApiResponse.success();
    }

    @PatchMapping("/order")
    public ApiResponse<Void> reorderStations(@Valid @RequestBody StationReorderRequest request) {
        stationService.reorderStations(request);
        return ApiResponse.success();
    }
    @PatchMapping("/{id}/activate")
    public ApiResponse<Void> activateStation(@PathVariable Long id) {
        stationService.activateStation(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<Void> deactivateStation(@PathVariable Long id) {
        stationService.deactivateStation(id);
        return ApiResponse.success();
    }

}
