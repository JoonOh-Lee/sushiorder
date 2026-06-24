package com.joonoh.sushiorder.domain.staffcall.service;

import com.joonoh.sushiorder.domain.staffcall.dto.StaffCallResponse;
import com.joonoh.sushiorder.domain.staffcall.entity.CallStatus;
import com.joonoh.sushiorder.domain.staffcall.entity.CallType;
import com.joonoh.sushiorder.domain.staffcall.entity.StaffCall;
import com.joonoh.sushiorder.domain.staffcall.exception.StaffCallNotFoundException;
import com.joonoh.sushiorder.domain.staffcall.repository.StaffCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffCallService {

    /** 직원 대시보드가 구독하는 실시간 알림 토픽 — 호출 생성/처리 시 같은 채널로 브로드캐스트 */
    private static final String STAFF_CALL_TOPIC = "/topic/staff/calls";

    private final StaffCallRepository staffCallRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public StaffCallResponse createCall(Long tableId, Long sessionId, CallType type, String itemName) {
        if (staffCallRepository.existsByTableIdAndStatus(tableId, CallStatus.REQUESTED)) {
            throw new IllegalStateException("이미 처리 대기 중인 호출이 있습니다.");
        }

        StaffCall call = StaffCall.builder()
                .tableId(tableId)
                .sessionId(sessionId)
                .type(type)
                .itemName(itemName)
                .build();

        StaffCallResponse response = StaffCallResponse.from(staffCallRepository.save(call));
        messagingTemplate.convertAndSend(STAFF_CALL_TOPIC, response);
        return response;
    }

    /** 처리 대기 중인 호출 목록 — 직원 대시보드용 */
    public List<StaffCallResponse> getRequestedCalls() {
        return staffCallRepository.findByStatusOrderByCreatedAtAsc(CallStatus.REQUESTED).stream()
                .map(StaffCallResponse::from)
                .toList();
    }

    @Transactional
    public void resolveCall(Long id) {
        StaffCall call = findCallOrThrow(id);
        call.resolve();
        messagingTemplate.convertAndSend(STAFF_CALL_TOPIC, StaffCallResponse.from(call));
    }

    private StaffCall findCallOrThrow(Long id) {
        return staffCallRepository.findById(id)
                .orElseThrow(() -> new StaffCallNotFoundException(id));
    }
}
