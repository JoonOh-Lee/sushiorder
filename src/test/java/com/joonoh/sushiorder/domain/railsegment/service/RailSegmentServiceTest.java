package com.joonoh.sushiorder.domain.railsegment.service;

import com.joonoh.sushiorder.domain.railsegment.dto.RailSegmentResponse;
import com.joonoh.sushiorder.domain.railsegment.entity.RailSegment;
import com.joonoh.sushiorder.domain.railsegment.exception.RailSegmentNotFoundException;
import com.joonoh.sushiorder.domain.railsegment.repository.RailSegmentRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class RailSegmentServiceTest {

    @Autowired private RailSegmentService railSegmentService;
    @Autowired private RailSegmentRepository railSegmentRepository;
    @Autowired private RestaurantTableRepository restaurantTableRepository;

    private Long fromTableId;
    private Long toTableId;
    private Long segmentId;

    @BeforeEach
    void setUp() {
        fromTableId = restaurantTableRepository.saveAndFlush(
                RestaurantTable.builder().seatType(SeatType.TABLE).tableNumber(9101).seatCount(4).build()).getId();
        toTableId = restaurantTableRepository.saveAndFlush(
                RestaurantTable.builder().seatType(SeatType.TABLE).tableNumber(9102).seatCount(4).build()).getId();
    }

    @AfterEach
    void tearDown() {
        if (segmentId != null) {
            railSegmentRepository.deleteById(segmentId);
            segmentId = null;
        }
        restaurantTableRepository.deleteById(fromTableId);
        restaurantTableRepository.deleteById(toTableId);
    }

    @Test
    @DisplayName("조회 시 sequenceOrder 기준으로 정렬되어 반환된다")
    void getAllSegments_sortedBySequenceOrder() {
        RailSegment second = railSegmentRepository.save(
                RailSegment.builder().sequenceOrder(9999).fromTableId(fromTableId).toTableId(toTableId).build());
        RailSegment first = railSegmentRepository.save(
                RailSegment.builder().sequenceOrder(1).fromTableId(toTableId).toTableId(fromTableId).build());

        List<RailSegmentResponse> results = railSegmentService.getAllSegments();

        int firstIdx = indexOf(results, first.getId());
        int secondIdx = indexOf(results, second.getId());
        assertThat(firstIdx).isLessThan(secondIdx);

        railSegmentRepository.deleteById(first.getId());
        railSegmentRepository.deleteById(second.getId());
    }

    @Test
    @DisplayName("새 구간은 active=true로 시작하고, deactivate/activate로 토글할 수 있다")
    void deactivateThenActivate_togglesState() {
        RailSegment segment = railSegmentRepository.save(
                RailSegment.builder().sequenceOrder(1).fromTableId(fromTableId).toTableId(toTableId).build());
        segmentId = segment.getId();
        assertThat(segment.isActive()).isTrue();

        RailSegmentResponse deactivated = railSegmentService.deactivate(segmentId);
        assertThat(deactivated.isActive()).isFalse();

        RailSegmentResponse activated = railSegmentService.activate(segmentId);
        assertThat(activated.isActive()).isTrue();
    }

    @Test
    @DisplayName("이미 비활성화된 구간을 다시 비활성화하면 예외가 발생한다")
    void deactivate_alreadyInactive_throws() {
        RailSegment segment = railSegmentRepository.save(
                RailSegment.builder().sequenceOrder(1).fromTableId(fromTableId).toTableId(toTableId).build());
        segmentId = segment.getId();
        railSegmentService.deactivate(segmentId);

        assertThatThrownBy(() -> railSegmentService.deactivate(segmentId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("존재하지 않는 구간은 활성화할 수 없다")
    void activate_notFound_throws() {
        assertThatThrownBy(() -> railSegmentService.activate(999_999L))
                .isInstanceOf(RailSegmentNotFoundException.class);
    }

    private int indexOf(List<RailSegmentResponse> responses, Long id) {
        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).getId().equals(id)) {
                return i;
            }
        }
        throw new AssertionError("not found: " + id);
    }
}
