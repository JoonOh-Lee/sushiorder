package com.joonoh.sushiorder.domain.menu.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class StockSetRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("재고를 null로 두면(무제한 전환) 검증을 통과한다")
    void nullStockCount_isValid() {
        StockSetRequest request = requestOf(null);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    @DisplayName("재고를 0 이상으로 두면 검증을 통과한다")
    void zeroOrPositiveStockCount_isValid() {
        assertThat(validator.validate(requestOf(0))).isEmpty();
        assertThat(validator.validate(requestOf(10))).isEmpty();
    }

    @Test
    @DisplayName("재고를 음수로 두면 검증에 실패한다")
    void negativeStockCount_isInvalid() {
        Set<ConstraintViolation<StockSetRequest>> violations = validator.validate(requestOf(-1));

        assertThat(violations).isNotEmpty();
    }

    private StockSetRequest requestOf(Integer stockCount) {
        StockSetRequest request = new StockSetRequest();
        setField(request, "stockCount", stockCount);
        return request;
    }

    private void setField(Object target, String name, Object value) {
        try {
            var field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
