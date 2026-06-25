package com.joonoh.sushiorder.domain.restauranttable.service;

import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableCreateRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTablePositionRequest;
import com.joonoh.sushiorder.domain.restauranttable.dto.RestaurantTableResponse;
import com.joonoh.sushiorder.domain.restauranttable.entity.SeatType;
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
        RestaurantTableResponse response = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8001, 4));
        tableId = response.getId();

        assertThat(response.getStatus()).isEqualTo(TableStatus.EMPTY);
        assertThat(response.getSeatType()).isEqualTo(SeatType.TABLE);
        assertThat(response.getTableNumber()).isEqualTo(8001);
        assertThat(response.getSeatCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("같은 좌석 타입에서 이미 존재하는 테이블 번호로는 생성할 수 없다")
    void createTable_duplicateTableNumber_throws() {
        RestaurantTableResponse response = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8002, 4));
        tableId = response.getId();

        assertThatThrownBy(() -> restaurantTableService.createTable(createRequest(SeatType.TABLE, 8002, 2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("좌석 타입이 다르면 같은 테이블 번호도 생성할 수 있다")
    void createTable_sameNumberDifferentSeatType_succeeds() {
        RestaurantTableResponse tableResponse = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8006, 4));
        tableId = tableResponse.getId();

        RestaurantTableResponse counterResponse = restaurantTableService.createTable(createRequest(SeatType.COUNTER, 8006, 1));

        assertThat(counterResponse.getSeatType()).isEqualTo(SeatType.COUNTER);
        assertThat(counterResponse.getTableNumber()).isEqualTo(8006);

        restaurantTableRepository.deleteById(counterResponse.getId());
    }

    @Test
    @DisplayName("occupy 후 release하면 다시 EMPTY로 돌아온다")
    void occupyThenRelease_changesStatus() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8003, 4));
        tableId = created.getId();

        restaurantTableService.occupyTable(tableId);
        assertThat(restaurantTableService.getTable(tableId).getStatus()).isEqualTo(TableStatus.OCCUPIED);

        restaurantTableService.releaseTable(tableId);
        assertThat(restaurantTableService.getTable(tableId).getStatus()).isEqualTo(TableStatus.EMPTY);
    }

    @Test
    @DisplayName("이미 사용 중인 테이블은 다시 occupy할 수 없다")
    void occupyTable_alreadyOccupied_throws() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8004, 4));
        tableId = created.getId();
        restaurantTableService.occupyTable(tableId);

        assertThatThrownBy(() -> restaurantTableService.occupyTable(tableId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("reserve 후 cancelReservation하면 다시 EMPTY로 돌아온다")
    void reserveThenCancel_changesStatus() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8005, 4));
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

    @Test
    @DisplayName("새로 만든 테이블은 평면도 위치가 비어있고, 위치를 수정하면 반영된다")
    void updatePosition_setsCoordinates() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8007, 4));
        tableId = created.getId();
        assertThat(created.getX()).isNull();

        RestaurantTableResponse updated = restaurantTableService.updatePosition(
                tableId, positionRequest(10.5, 20.0, 15.0, 15.0));

        assertThat(updated.getX()).isEqualTo(10.5);
        assertThat(updated.getY()).isEqualTo(20.0);
        assertThat(updated.getWidth()).isEqualTo(15.0);
        assertThat(updated.getHeight()).isEqualTo(15.0);
    }

    @Test
    @DisplayName("0~100 범위를 벗어난 좌표로는 위치를 수정할 수 없다")
    void updatePosition_outOfRange_throws() {
        RestaurantTableResponse created = restaurantTableService.createTable(createRequest(SeatType.TABLE, 8008, 4));
        tableId = created.getId();

        assertThatThrownBy(() -> restaurantTableService.updatePosition(
                tableId, positionRequest(101.0, 20.0, 15.0, 15.0)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private RestaurantTablePositionRequest positionRequest(double x, double y, double width, double height) {
        RestaurantTablePositionRequest request = new RestaurantTablePositionRequest();
        setField(request, "x", x);
        setField(request, "y", y);
        setField(request, "width", width);
        setField(request, "height", height);
        return request;
    }

    private RestaurantTableCreateRequest createRequest(SeatType seatType, int tableNumber, int seatCount) {
        RestaurantTableCreateRequest request = new RestaurantTableCreateRequest();
        setField(request, "seatType", seatType);
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
