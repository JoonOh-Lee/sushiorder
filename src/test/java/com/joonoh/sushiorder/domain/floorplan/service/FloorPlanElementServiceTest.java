package com.joonoh.sushiorder.domain.floorplan.service;

import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementCreateRequest;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementPositionRequest;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementResponse;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementUpdateRequest;
import com.joonoh.sushiorder.domain.floorplan.entity.FloorPlanElementType;
import com.joonoh.sushiorder.domain.floorplan.exception.FloorPlanElementNotFoundException;
import com.joonoh.sushiorder.domain.floorplan.repository.FloorPlanElementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FloorPlanElementServiceTest {

    @Autowired private FloorPlanElementService floorPlanElementService;
    @Autowired private FloorPlanElementRepository floorPlanElementRepository;

    private Long elementId;

    @AfterEach
    void tearDown() {
        if (elementId != null) {
            floorPlanElementRepository.deleteById(elementId);
            elementId = null;
        }
    }

    @Test
    @DisplayName("생성 시 입력한 타입/이름/좌표가 그대로 저장된다")
    void createElement_savesGivenValues() {
        FloorPlanElementResponse response = floorPlanElementService.createElement(
                createRequest(FloorPlanElementType.KITCHEN, "주방", 42.0, 42.0, 16.0, 16.0));
        elementId = response.getId();

        assertThat(response.getType()).isEqualTo(FloorPlanElementType.KITCHEN);
        assertThat(response.getLabel()).isEqualTo("주방");
        assertThat(response.getX()).isEqualTo(42.0);
        assertThat(response.getY()).isEqualTo(42.0);
        assertThat(response.getWidth()).isEqualTo(16.0);
        assertThat(response.getHeight()).isEqualTo(16.0);
    }

    @Test
    @DisplayName("0~100 범위를 벗어난 좌표로는 생성할 수 없다")
    void createElement_outOfRange_throws() {
        assertThatThrownBy(() -> floorPlanElementService.createElement(
                createRequest(FloorPlanElementType.RAIL, "레일", 101.0, 30.0, 40.0, 40.0)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("위치를 수정하면 좌표가 갱신된다")
    void updatePosition_updatesCoordinates() {
        FloorPlanElementResponse created = floorPlanElementService.createElement(
                createRequest(FloorPlanElementType.RAIL, "레일", 30.0, 30.0, 40.0, 40.0));
        elementId = created.getId();

        FloorPlanElementResponse updated = floorPlanElementService.updatePosition(
                elementId, positionRequest(20.0, 20.0, 50.0, 50.0));

        assertThat(updated.getX()).isEqualTo(20.0);
        assertThat(updated.getY()).isEqualTo(20.0);
        assertThat(updated.getWidth()).isEqualTo(50.0);
        assertThat(updated.getHeight()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("타입/이름을 수정하면 좌표는 그대로 유지된다")
    void updateInfo_changesTypeAndLabel_keepsPosition() {
        FloorPlanElementResponse created = floorPlanElementService.createElement(
                createRequest(FloorPlanElementType.ETC, "임시", 10.0, 10.0, 5.0, 5.0));
        elementId = created.getId();

        FloorPlanElementResponse updated = floorPlanElementService.updateInfo(
                elementId, updateRequest(FloorPlanElementType.KITCHEN, "주방"));

        assertThat(updated.getType()).isEqualTo(FloorPlanElementType.KITCHEN);
        assertThat(updated.getLabel()).isEqualTo("주방");
        assertThat(updated.getX()).isEqualTo(10.0);
        assertThat(updated.getY()).isEqualTo(10.0);
    }

    @Test
    @DisplayName("삭제하면 더 이상 조회되지 않는다")
    void deleteElement_removesIt() {
        FloorPlanElementResponse created = floorPlanElementService.createElement(
                createRequest(FloorPlanElementType.ETC, "삭제 테스트", 1.0, 1.0, 5.0, 5.0));
        Long id = created.getId();

        floorPlanElementService.deleteElement(id);

        assertThatThrownBy(() -> floorPlanElementService.getElement(id))
                .isInstanceOf(FloorPlanElementNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 요소는 조회할 수 없다")
    void getElement_notFound_throws() {
        assertThatThrownBy(() -> floorPlanElementService.getElement(999_999L))
                .isInstanceOf(FloorPlanElementNotFoundException.class);
    }

    private FloorPlanElementCreateRequest createRequest(
            FloorPlanElementType type, String label, Double x, Double y, Double width, Double height) {
        FloorPlanElementCreateRequest request = new FloorPlanElementCreateRequest();
        setField(request, "type", type);
        setField(request, "label", label);
        setField(request, "x", x);
        setField(request, "y", y);
        setField(request, "width", width);
        setField(request, "height", height);
        return request;
    }

    private FloorPlanElementPositionRequest positionRequest(Double x, Double y, Double width, Double height) {
        FloorPlanElementPositionRequest request = new FloorPlanElementPositionRequest();
        setField(request, "x", x);
        setField(request, "y", y);
        setField(request, "width", width);
        setField(request, "height", height);
        return request;
    }

    private FloorPlanElementUpdateRequest updateRequest(FloorPlanElementType type, String label) {
        FloorPlanElementUpdateRequest request = new FloorPlanElementUpdateRequest();
        setField(request, "type", type);
        setField(request, "label", label);
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
