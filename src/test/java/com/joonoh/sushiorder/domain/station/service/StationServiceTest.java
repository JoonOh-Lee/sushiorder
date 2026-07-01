package com.joonoh.sushiorder.domain.station.service;

import com.joonoh.sushiorder.domain.station.dto.StationCreateRequest;
import com.joonoh.sushiorder.domain.station.dto.StationOrderItem;
import com.joonoh.sushiorder.domain.station.dto.StationRenameRequest;
import com.joonoh.sushiorder.domain.station.dto.StationReorderRequest;
import com.joonoh.sushiorder.domain.station.dto.StationResponse;
import com.joonoh.sushiorder.domain.station.exception.StationNotFoundException;
import com.joonoh.sushiorder.domain.station.repository.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StationServiceTest {

    @Autowired private StationService stationService;
    @Autowired private StationRepository stationRepository;

    private Long stationId;

    @AfterEach
    void tearDown() {
        if (stationId != null) {
            stationRepository.deleteById(stationId);
            stationId = null;
        }
    }

    @Test
    @DisplayName("station을 생성하면 활성 상태로 시작한다")
    void createStation_startsActive() {
        StationResponse response = stationService.createStation(createRequest("초밥"));
        stationId = response.getId();

        assertThat(response.isActive()).isTrue();
        assertThat(response.getName()).isEqualTo("초밥");
    }

    @Test
    @DisplayName("생성한 station은 활성 목록/전체 목록에 모두 나온다")
    void getStations_includesCreatedStation() {
        StationResponse response = stationService.createStation(createRequest("튀김"));
        stationId = response.getId();

        assertThat(stationService.getActiveStations())
                .extracting(StationResponse::getId)
                .contains(stationId);
        assertThat(stationService.getAllStations())
                .extracting(StationResponse::getId)
                .contains(stationId);
    }

    @Test
    @DisplayName("이름을 바꾸면 반영된다")
    void renameStation_changesName() {
        StationResponse response = stationService.createStation(createRequest("초밥"));
        stationId = response.getId();

        stationService.renameStation(stationId, renameRequest("특선초밥"));

        assertThat(stationService.getAllStations().stream()
                .filter(s -> s.getId().equals(stationId))
                .findFirst().orElseThrow()
                .getName()).isEqualTo("특선초밥");
    }

    @Test
    @DisplayName("존재하지 않는 station은 이름을 바꿀 수 없다")
    void renameStation_notFound_throws() {
        assertThatThrownBy(() -> stationService.renameStation(999_999L, renameRequest("특선초밥")))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("비활성화하면 활성 목록에서 빠지고, 재활성화하면 다시 나온다")
    void deactivateThenActivate_togglesVisibilityInActiveList() {
        StationResponse response = stationService.createStation(createRequest("디저트"));
        stationId = response.getId();

        stationService.deactivateStation(stationId);
        assertThat(stationService.getActiveStations())
                .extracting(StationResponse::getId)
                .doesNotContain(stationId);

        stationService.activateStation(stationId);
        assertThat(stationService.getActiveStations())
                .extracting(StationResponse::getId)
                .contains(stationId);
    }

    @Test
    @DisplayName("순서를 재배치하면 각 station의 sortOrder가 바뀐다")
    void reorderStations_updatesSortOrder() {
        StationResponse first = stationService.createStation(createRequest("초밥"));
        StationResponse second = stationService.createStation(createRequest("튀김"));

        try {
            StationReorderRequest request = new StationReorderRequest();
            setField(request, "orders", List.of(
                    orderItem(first.getId(), 10),
                    orderItem(second.getId(), 5)
            ));

            stationService.reorderStations(request);

            List<StationResponse> all = stationService.getAllStations();
            assertThat(all.stream().filter(s -> s.getId().equals(first.getId())).findFirst().orElseThrow().getSortOrder())
                    .isEqualTo(10);
            assertThat(all.stream().filter(s -> s.getId().equals(second.getId())).findFirst().orElseThrow().getSortOrder())
                    .isEqualTo(5);
        } finally {
            stationRepository.deleteById(second.getId());
            stationId = first.getId();
        }
    }

    private StationOrderItem orderItem(Long stationId, int sortOrder) {
        StationOrderItem item = new StationOrderItem();
        setField(item, "stationId", stationId);
        setField(item, "sortOrder", sortOrder);
        return item;
    }

    private StationCreateRequest createRequest(String name) {
        StationCreateRequest request = new StationCreateRequest();
        setField(request, "name", name);
        return request;
    }

    private StationRenameRequest renameRequest(String name) {
        StationRenameRequest request = new StationRenameRequest();
        setField(request, "name", name);
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
