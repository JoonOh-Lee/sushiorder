package com.joonoh.sushiorder.domain.restauranttable.service;

import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableCreateRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableResponse;
import com.joonoh.sushiorder.domain.restauranttable.entity.TableStatus;
import com.joonoh.sushiorder.domain.restauranttable.exception.RestaurantTableNotFoundException;
import com.joonoh.sushiorder.domain.restauranttable.repository.RestaurantTableRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class RestaurantTableServiceTest {

    @Autowired private RestaurantTableService restaurantTableService;
    @Autowired private RestaurantTableRepository restaurantTableRepository;

    private Long tableId;

    @AfterEach
    void tearDown() {
        if (tableId != null) {
            restaurantTableRepository.deleteById(tableId);
            tableId = null;
        }
    }

    @Test
    @DisplayName("테이블을 생성하면 EMPTY 상태로 시작한다")
    void createTable_startsEmpty() {
        RestaurantTableResponse response = restaurantTableService.createTable(createRequest(8001, 4));
        tableId = response.getId();

        assertThat(response.getStatus()).isEqualTo(TableStatus.EMPTY);
        assertThat(response.getTableNumber()).isEqualTo(8001);
        assertThat(response.getSeatCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("이미 존재하는 테이블 번호로는 생성할 수 없다")
    void createTable_duplicateTableNumber_throws() {
        RestaurantTableResponse response = restaurantTableService.createTable(createRequest(8002, 4));
        tableId = response.getId();

        assertThatThrownBy(() -> restaurantTableService.createTable(createRequest(8002, 2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("occupy 후 release하면 다시 EMPTY로 돌아온다")
    void occupyThenRelease_changesStatus() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(8003, 4));
        tableId = created.getId();

        restaurantTableService.occupyTable(tableId);
        assertThat(restaurantTableService.getTable(tableId).getStatus()).isEqualTo(TableStatus.OCCUPIED);

        restaurantTableService.releaseTable(tableId);
        assertThat(restaurantTableService.getTable(tableId).getStatus()).isEqualTo(TableStatus.EMPTY);
    }

    @Test
    @DisplayName("이미 사용 중인 테이블은 다시 occupy할 수 없다")
    void occupyTable_alreadyOccupied_throws() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(8004, 4));
        tableId = created.getId();
        restaurantTableService.occupyTable(tableId);

        assertThatThrownBy(() -> restaurantTableService.occupyTable(tableId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("reserve 후 cancelReservation하면 다시 EMPTY로 돌아온다")
    void reserveThenCancel_changesStatus() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(8005, 4));
        tableId = created.getId();

        restaurantTableService.reserveTable(tableId);
        assertThat(restaurantTableService.getTable(tableId).getStatus()).isEqualTo(TableStatus.RESERVED);

        restaurantTableService.cancelReservation(tableId);
        assertThat(restaurantTableService.getTable(tableId).getStatus()).isEqualTo(TableStatus.EMPTY);
    }

    @Test
    @DisplayName("존재하지 않는 테이블은 조회할 수 없다")
    void getTable_notFound_throws() {
        assertThatThrownBy(() -> restaurantTableService.getTable(999_999L))
                .isInstanceOf(RestaurantTableNotFoundException.class);
    }

    private RestaurantTableCreateRequest createRequest(int tableNumber, int seatCount) {
        RestaurantTableCreateRequest request = new RestaurantTableCreateRequest();
        setField(request, "tableNumber", tableNumber);
        setField(request, "seatCount", seatCount);
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
