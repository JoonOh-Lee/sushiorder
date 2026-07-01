package com.joonoh.sushiorder.domain.order.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceOrderRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("아이템 개수가 50개 이하면 검증을 통과한다")
    void upToFiftyItems_isValid() {
        PlaceOrderRequest request = requestWithItems(50);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    @DisplayName("아이템이 50개를 넘으면 검증에 실패한다")
    void moreThanFiftyItems_isInvalid() {
        PlaceOrderRequest request = requestWithItems(51);

        assertThat(validator.validate(request)).isNotEmpty();
    }

    @Test
    @DisplayName("수량이 99를 넘으면 검증에 실패한다")
    void quantityOverMax_isInvalid() {
        OrderItemRequest item = itemOf(1L, 100);

        assertThat(validator.validate(item)).isNotEmpty();
    }

    @Test
    @DisplayName("수량이 99 이하면 검증을 통과한다")
    void quantityAtMax_isValid() {
        OrderItemRequest item = itemOf(1L, 99);

        assertThat(validator.validate(item)).isEmpty();
    }

    private PlaceOrderRequest requestWithItems(int count) {
        List<OrderItemRequest> items = IntStream.rangeClosed(1, count)
                .mapToObj(i -> itemOf((long) i, 1))
                .toList();

        PlaceOrderRequest request = new PlaceOrderRequest();
        setField(request, "idempotencyKey", "test-key");
        setField(request, "items", items);
        return request;
    }

    private OrderItemRequest itemOf(Long menuId, Integer quantity) {
        OrderItemRequest item = new OrderItemRequest();
        setField(item, "menuId", menuId);
        setField(item, "quantity", quantity);
        return item;
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
