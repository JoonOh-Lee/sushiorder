package com.joonoh.sushiorder.domain.staffcall.service;

import com.joonoh.sushiorder.domain.staffcall.dto.StaffCallResponse;
import com.joonoh.sushiorder.domain.staffcall.entity.CallStatus;
import com.joonoh.sushiorder.domain.staffcall.entity.CallType;
import com.joonoh.sushiorder.domain.staffcall.entity.StaffCall;
import com.joonoh.sushiorder.domain.staffcall.exception.StaffCallNotFoundException;
import com.joonoh.sushiorder.domain.staffcall.repository.StaffCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffCallService {

    private final StaffCallRepository staffCallRepository;

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

        return StaffCallResponse.from(staffCallRepository.save(call));
    }

    /** 처리 대기 중인 호출 목록 — 직원 대시보드용 */
    public List<StaffCallResponse> getRequestedCalls() {
        return staffCallRepository.findByStatusOrderByCreatedAtAsc(CallStatus.REQUESTED).stream()
                .map(StaffCallResponse::from)
                .toList();
    }

    @Transactional
    public void resolveCall(Long id) {
        findCallOrThrow(id).resolve();
    }

    private StaffCall findCallOrThrow(Long id) {
        return staffCallRepository.findById(id)
                .orElseThrow(() -> new StaffCallNotFoundException(id));
    }
}
