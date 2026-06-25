package com.joonoh.sushiorder.global.exception;

import com.joonoh.sushiorder.domain.floorplan.exception.FloorPlanElementNotFoundException;
import com.joonoh.sushiorder.domain.menu.exception.MenuNotFoundException;
import com.joonoh.sushiorder.domain.notice.exception.NoticeNotFoundException;
import com.joonoh.sushiorder.domain.order.exception.OrderNotFoundException;
import com.joonoh.sushiorder.domain.restauranttable.exception.RestaurantTableNotFoundException;
import com.joonoh.sushiorder.domain.session.exception.InvalidSessionTokenException;
import com.joonoh.sushiorder.domain.session.exception.TableSessionNotFoundException;
import com.joonoh.sushiorder.domain.staff.exception.InvalidCredentialsException;
import com.joonoh.sushiorder.domain.staff.exception.StaffNotFoundException;
import com.joonoh.sushiorder.domain.staffcall.exception.StaffCallNotFoundException;
import com.joonoh.sushiorder.domain.station.exception.StationNotFoundException;
import com.joonoh.sushiorder.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMenuNotFound(MenuNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(message));
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStationNotFound(StationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public  ResponseEntity<ApiResponse<Void>> handleStationNotFound(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(RestaurantTableNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRestaurantTableNotFound(RestaurantTableNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(TableSessionNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleTableSessionNotFound(TableSessionNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(InvalidSessionTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidSessionToken(InvalidSessionTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(StaffNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStaffNotFound(StaffNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(StaffCallNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStaffCallNotFound(StaffCallNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoticeNotFound(NoticeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(FloorPlanElementNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleFloorPlanElementNotFound(FloorPlanElementNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(e.getMessage()));
    }
}
