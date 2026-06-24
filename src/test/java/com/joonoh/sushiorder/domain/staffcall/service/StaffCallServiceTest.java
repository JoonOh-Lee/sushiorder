package com.joonoh.sushiorder.domain.staffcall.service;

import com.joonoh.sushiorder.domain.staffcall.dto.StaffCallResponse;
import com.joonoh.sushiorder.domain.staffcall.entity.CallStatus;
import com.joonoh.sushiorder.domain.staffcall.entity.CallType;
import com.joonoh.sushiorder.domain.staffcall.exception.StaffCallNotFoundException;
import com.joonoh.sushiorder.domain.staffcall.repository.StaffCallRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class StaffCallServiceTest {

    @Autowired private StaffCallService staffCallService;
    @Autowired private StaffCallRepository staffCallRepository;

    private Long callId;

    @AfterEach
    void tearDown() {
        if (callId != null) {
            staffCallRepository.deleteById(callId);
            callId = null;
        }
    }

    @Test
    @DisplayName("호출을 생성하면 REQUESTED 상태로 시작한다")
    void createCall_startsRequested() {
        StaffCallResponse response = staffCallService.createCall(7001L, 1L, CallType.WATER_REFILL, null);
        callId = response.getId();

        assertThat(response.getStatus()).isEqualTo(CallStatus.REQUESTED);
        assertThat(response.getTableId()).isEqualTo(7001L);
        assertThat(response.getType()).isEqualTo(CallType.WATER_REFILL);
    }

    @Test
    @DisplayName("같은 테이블에 처리 대기 중인 호출이 있으면 중복 생성할 수 없다")
    void createCall_duplicatePendingCall_throws() {
        StaffCallResponse response = staffCallService.createCall(7002L, 1L, CallType.INQUIRY, null);
        callId = response.getId();

        assertThatThrownBy(() -> staffCallService.createCall(7002L, 1L, CallType.OTHER, null))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("처리 완료된 호출이 있는 테이블은 다시 호출할 수 있다")
    void createCall_afterResolved_succeeds() {
        StaffCallResponse first = staffCallService.createCall(7003L, 1L, CallType.WATER_REFILL, null);
        staffCallService.resolveCall(first.getId());

        StaffCallResponse second = staffCallService.createCall(7003L, 1L, CallType.INQUIRY, null);
        callId = second.getId();

        assertThat(staffCallService.getRequestedCalls())
                .extracting(StaffCallResponse::getId)
                .contains(second.getId());

        staffCallRepository.deleteById(first.getId());
    }

    @Test
    @DisplayName("resolve하면 처리 대기 목록에서 빠진다")
    void resolveCall_removesFromRequestedList() {
        StaffCallResponse response = staffCallService.createCall(7004L, 1L, CallType.OTHER, null);
        callId = response.getId();

        staffCallService.resolveCall(callId);

        assertThat(staffCallService.getRequestedCalls())
                .extracting(StaffCallResponse::getId)
                .doesNotContain(callId);
    }

    @Test
    @DisplayName("이미 처리된 호출은 다시 resolve할 수 없다")
    void resolveCall_alreadyResolved_throws() {
        StaffCallResponse response = staffCallService.createCall(7005L, 1L, CallType.OTHER, null);
        callId = response.getId();
        staffCallService.resolveCall(callId);

        assertThatThrownBy(() -> staffCallService.resolveCall(callId))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("존재하지 않는 호출은 resolve할 수 없다")
    void resolveCall_notFound_throws() {
        assertThatThrownBy(() -> staffCallService.resolveCall(999_999L))
                .isInstanceOf(StaffCallNotFoundException.class);
    }

    @Test
    @DisplayName("ITEM_REQUEST는 물품명과 함께 호출할 수 있다")
    void createCall_itemRequest_succeeds() {
        StaffCallResponse response = staffCallService.createCall(7006L, 1L, CallType.ITEM_REQUEST, "장국추가");
        callId = response.getId();

        assertThat(response.getType()).isEqualTo(CallType.ITEM_REQUEST);
        assertThat(response.getItemName()).isEqualTo("장국추가");
    }

    @Test
    @DisplayName("ITEM_REQUEST는 물품명 없이 호출할 수 없다")
    void createCall_itemRequestWithoutItemName_throws() {
        assertThatThrownBy(() -> staffCallService.createCall(7007L, 1L, CallType.ITEM_REQUEST, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
