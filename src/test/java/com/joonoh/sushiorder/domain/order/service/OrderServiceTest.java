package com.joonoh.sushiorder.domain.order.service;

import com.joonoh.sushiorder.domain.menu.entity.Menu;
import com.joonoh.sushiorder.domain.menu.entity.MenuCategory;
import com.joonoh.sushiorder.domain.menu.repository.MenuRepository;
import com.joonoh.sushiorder.domain.order.dto.OrderItemRequest;
import com.joonoh.sushiorder.domain.order.dto.OrderItemResponse;
import com.joonoh.sushiorder.domain.order.dto.OrderResponse;
import com.joonoh.sushiorder.domain.order.dto.PlaceOrderRequest;
import com.joonoh.sushiorder.domain.order.entity.OrderStatus;
import com.joonoh.sushiorder.domain.order.repository.OrderRepository;
import com.joonoh.sushiorder.domain.restauranttable.entity.RestaurantTable;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
import com.joonoh.sushiorder.domain.restauranttable.repository.RestaurantTableRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
class OrderServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private MenuRepository menuRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private RestaurantTableRepository restaurantTableRepository;

    private Long menuId;
    private Long otherStationMenuId;
    private Long limitedStockMenuId;
    private Long tableId;

    @BeforeEach
    void setUp() {
        Menu menu = Menu.builder()
                .name("주문 테스트용 초밥")
                .price(1990)
                .category(MenuCategory.FRESH_SUSHI)
                .stationId(1L)
                .build();
        menuId = menuRepository.saveAndFlush(menu).getId();

        Menu otherStationMenu = Menu.builder()
                .name("주문 테스트용 다른 station 초밥")
                .price(1990)
                .category(MenuCategory.MEAT_SUSHI)
                .stationId(2L)
                .build();
        otherStationMenuId = menuRepository.saveAndFlush(otherStationMenu).getId();

        Menu limitedStockMenu = Menu.builder()
                .name("주문 테스트용 한정수량 초밥")
                .price(1990)
                .category(MenuCategory.FRESH_SUSHI)
                .stockCount(5)
                .stationId(1L)
                .build();
        limitedStockMenuId = menuRepository.saveAndFlush(limitedStockMenu).getId();

        RestaurantTable table = RestaurantTable.builder()
                .seatType(SeatType.TABLE)
                .tableNumber(9001)
                .seatCount(4)
                .build();
        tableId = restaurantTableRepository.saveAndFlush(table).getId();
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        menuRepository.deleteById(menuId);
        menuRepository.deleteById(otherStationMenuId);
        menuRepository.deleteById(limitedStockMenuId);
        restaurantTableRepository.deleteById(tableId);
    }

    @Test
    @DisplayName("주문 생성 시 실제 테이블이 있으면 seatType/tableNumber가 채워진다")
    void placeOrder_fillsTableInfo_whenTableExists() {
        OrderResponse response = orderService.placeOrder(tableId, 1L, placeOrderRequest(menuId, 1));

        assertThat(response.getSeatType()).isEqualTo(SeatType.TABLE);
        assertThat(response.getTableNumber()).isEqualTo(9001);
    }

    @Test
    @DisplayName("주문의 tableId가 실제 테이블과 매칭되지 않아도 주문 처리는 실패하지 않는다")
    void placeOrder_doesNotFail_whenTableNotFound() {
        long nonExistentTableId = 999_999L;

        assertThatNoException().isThrownBy(() ->
                orderService.placeOrder(nonExistentTableId, 1L, placeOrderRequest(menuId, 1)));

        OrderResponse response = orderService.placeOrder(nonExistentTableId, 2L,
                placeOrderRequest(menuId, 1));
        assertThat(response.getSeatType()).isNull();
        assertThat(response.getTableNumber()).isZero();
    }

    @Test
    @DisplayName("stationId로 필터링하면 해당 station 메뉴가 포함된 주문만 조회된다")
    void getOrdersByStatus_filtersByStation() {
        OrderResponse station1Order = orderService.placeOrder(tableId, 1L, placeOrderRequest(menuId, 1));
        OrderResponse station2Order = orderService.placeOrder(tableId, 2L, placeOrderRequest(otherStationMenuId, 1));

        List<OrderResponse> station1Results = orderService.getOrdersByStatus(List.of(OrderStatus.PENDING), 1L);

        assertThat(station1Results)
                .extracting(OrderResponse::getId)
                .contains(station1Order.getId())
                .doesNotContain(station2Order.getId());
    }

    @Test
    @DisplayName("stationId 없이 조회하면 station과 무관하게 전부 조회된다")
    void getOrdersByStatus_withoutStation_returnsAll() {
        OrderResponse order = orderService.placeOrder(tableId, 1L, placeOrderRequest(menuId, 1));

        List<OrderResponse> results = orderService.getOrdersByStatus(List.of(OrderStatus.PENDING), null);

        assertThat(results).extracting(OrderResponse::getId).contains(order.getId());
    }

    @Test
    @DisplayName("status를 여러 개 주면 PENDING+CONFIRMED 주문을 한 번에 조회할 수 있다 (station 화면용)")
    void getOrdersByStatus_withMultipleStatuses_returnsPendingAndConfirmed() {
        OrderResponse pendingOrder = orderService.placeOrder(tableId, 1L, placeOrderRequest(menuId, 1));
        OrderResponse toConfirmOrder = orderService.placeOrder(tableId, 2L, placeOrderRequest(menuId, 1));
        orderService.confirmOrder(toConfirmOrder.getId());

        List<OrderResponse> results = orderService.getOrdersByStatus(
                List.of(OrderStatus.PENDING, OrderStatus.CONFIRMED), 1L);

        assertThat(results)
                .extracting(OrderResponse::getId)
                .contains(pendingOrder.getId(), toConfirmOrder.getId());
    }

    @Test
    @DisplayName("station별로 부분 접수하면 해당 station 메뉴만 CONFIRMED로 전환되고, 다른 station 메뉴는 영향받지 않는다")
    void confirmOrderItemsByStation_onlyAffectsThatStation() {
        OrderResponse placed = orderService.placeOrder(tableId, 1L,
                placeOrderRequest(List.of(menuId, otherStationMenuId)));

        OrderResponse afterConfirm = orderService.confirmOrderItemsByStation(placed.getId(), 1L);

        assertThat(afterConfirm.getStatus()).isEqualTo(OrderStatus.PENDING); // station 2 메뉴가 아직 PENDING
        assertThat(itemFor(afterConfirm, menuId).getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(itemFor(afterConfirm, otherStationMenuId).getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("station별로 부분 완료 처리하면 해당 station 메뉴만 COMPLETED로 전환된다")
    void completeOrderItemsByStation_onlyCompletesThatStation() {
        OrderResponse placed = orderService.placeOrder(tableId, 1L,
                placeOrderRequest(List.of(menuId, otherStationMenuId)));
        orderService.confirmOrderItemsByStation(placed.getId(), 1L);
        orderService.confirmOrderItemsByStation(placed.getId(), 2L);

        OrderResponse afterComplete = orderService.completeOrderItemsByStation(placed.getId(), 1L);

        assertThat(afterComplete.getStatus()).isEqualTo(OrderStatus.CONFIRMED); // station 2 메뉴는 아직 CONFIRMED
        assertThat(itemFor(afterComplete, menuId).getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(itemFor(afterComplete, otherStationMenuId).getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    @DisplayName("한 station이 재료 소진으로 부분 취소해도 다른 station 메뉴는 그대로 접수할 수 있고, 취소된 메뉴 금액은 총액에서 빠진다")
    void cancelOrderItemsByStation_doesNotAffectOtherStation_andAdjustsTotalPrice() {
        OrderResponse placed = orderService.placeOrder(tableId, 1L,
                placeOrderRequest(List.of(menuId, otherStationMenuId)));
        int originalTotal = placed.getTotalPrice();

        OrderResponse afterCancel = orderService.cancelOrderItemsByStation(placed.getId(), 2L);

        assertThat(afterCancel.getTotalPrice()).isEqualTo(originalTotal - 1990);
        assertThat(afterCancel.getStatus()).isEqualTo(OrderStatus.PENDING); // station 1 메뉴는 여전히 PENDING
        assertThat(itemFor(afterCancel, otherStationMenuId).getStatus()).isEqualTo(OrderStatus.CANCELLED);

        OrderResponse afterConfirmStation1 = orderService.confirmOrderItemsByStation(placed.getId(), 1L);
        assertThat(afterConfirmStation1.getStatus()).isEqualTo(OrderStatus.CONFIRMED); // 취소된 메뉴는 계산에서 제외
    }

    @Test
    @DisplayName("CONFIRMED 상태였던 메뉴를 station이 부분 취소하면 재고가 복구된다")
    void cancelOrderItemsByStation_restoresStock_whenWasConfirmed() {
        OrderResponse placed = orderService.placeOrder(tableId, 1L, placeOrderRequest(limitedStockMenuId, 2));
        orderService.confirmOrderItemsByStation(placed.getId(), 1L);
        assertThat(menuRepository.findById(limitedStockMenuId).orElseThrow().getStockCount()).isEqualTo(3);

        orderService.cancelOrderItemsByStation(placed.getId(), 1L);

        assertThat(menuRepository.findById(limitedStockMenuId).orElseThrow().getStockCount()).isEqualTo(5);
    }

    @Test
    @DisplayName("이미 부분 취소된 station 메뉴가 있어도 전체 확정은 나머지 메뉴만 확정하고 취소된 메뉴는 그대로 둔다")
    void confirmOrder_skipsAlreadyCancelledStationItems() {
        OrderResponse placed = orderService.placeOrder(tableId, 1L,
                placeOrderRequest(List.of(menuId, otherStationMenuId)));
        orderService.cancelOrderItemsByStation(placed.getId(), 2L);

        OrderResponse afterConfirm = orderService.confirmOrder(placed.getId());

        assertThat(afterConfirm.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(itemFor(afterConfirm, menuId).getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(itemFor(afterConfirm, otherStationMenuId).getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    private OrderItemResponse itemFor(OrderResponse order, Long menuId) {
        return order.getItems().stream()
                .filter(item -> item.getMenuId().equals(menuId))
                .findFirst()
                .orElseThrow();
    }

    private PlaceOrderRequest placeOrderRequest(Long menuId, int quantity) {
        PlaceOrderRequest req = new PlaceOrderRequest();
        setField(req, "idempotencyKey", UUID.randomUUID().toString());

        OrderItemRequest item = new OrderItemRequest();
        setField(item, "menuId", menuId);
        setField(item, "quantity", quantity);
        setField(req, "items", List.of(item));
        return req;
    }

    private PlaceOrderRequest placeOrderRequest(List<Long> menuIds) {
        PlaceOrderRequest req = new PlaceOrderRequest();
        setField(req, "idempotencyKey", UUID.randomUUID().toString());

        List<OrderItemRequest> items = menuIds.stream().map(id -> {
            OrderItemRequest item = new OrderItemRequest();
            setField(item, "menuId", id);
            setField(item, "quantity", 1);
            return item;
        }).toList();
        setField(req, "items", items);
        return req;
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
