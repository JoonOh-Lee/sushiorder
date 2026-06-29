package com.joonoh.sushiorder.domain.restauranttable.controller;

import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableCreateRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTablePositionRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableResponse;
import com.joonoh.sushiorder.domain.restauranttable.service.RestaurantTableService;
import com.joonoh.sushiorder.global.common.ApiResponse;
import com.joonoh.sushiorder.global.common.QrCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/table")
@RequiredArgsConstructor
public class AdminRestaurantTableController {

    private final RestaurantTableService restaurantTableService;
    private final QrCodeService qrCodeService;

    /** 테이블 현황 전체 조회 (직원 앱 메인 화면) */
    @GetMapping
    public ApiResponse<List<RestaurantTableResponse>> getAllTables() {
        return ApiResponse.success(restaurantTableService.getAllTables());
    }

    @GetMapping("/{id}")
    public ApiResponse<RestaurantTableResponse> getTable(@PathVariable Long id) {
        return ApiResponse.success(restaurantTableService.getTable(id));
    }

    @PostMapping
    public ApiResponse<RestaurantTableResponse> createTable(@Valid @RequestBody RestaurantTableCreateRequest request) {
        return ApiResponse.success(restaurantTableService.createTable(request));
    }

    /** 워크인 손님 수동 착석 (QR 세션 없이) */
    @PatchMapping("/{id}/occupy")
    public ApiResponse<Void> occupyTable(@PathVariable Long id) {
        restaurantTableService.occupyTable(id);
        return ApiResponse.success();
    }

    /** 계산 완료 — 자리 비움 */
    @PatchMapping("/{id}/release")
    public ApiResponse<Void> releaseTable(@PathVariable Long id) {
        restaurantTableService.releaseTable(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/reserve")
    public ApiResponse<Void> reserveTable(@PathVariable Long id) {
        restaurantTableService.reserveTable(id);
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/cancel-reservation")
    public ApiResponse<Void> cancelReservation(@PathVariable Long id) {
        restaurantTableService.cancelReservation(id);
        return ApiResponse.success();
    }

    /** 테이블 QR 코드 이미지 다운로드 — 손님이 스캔하면 세션 시작 페이지로 이동 */
    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getTableQr(@PathVariable Long id) {
        restaurantTableService.getTable(id); // 테이블 존재 여부 확인
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeService.generateTableQr(id));
    }

    /** 매장 평면도 위치/크기 수정 (POS 화면 커스텀 배치) — ADMIN 전용 */
    @PatchMapping("/{id}/position")
    public ApiResponse<RestaurantTableResponse> updatePosition(
            @PathVariable Long id, @Valid @RequestBody RestaurantTablePositionRequest request) {
        return ApiResponse.success(restaurantTableService.updatePosition(id, request));
    }
}
