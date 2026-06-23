package com.joonoh.sushiorder.domain.staffcall.repository;

import com.joonoh.sushiorder.domain.staffcall.entity.CallStatus;
import com.joonoh.sushiorder.domain.staffcall.entity.StaffCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffCallRepository extends JpaRepository<StaffCall, Long> {

    List<StaffCall> findByStatusOrderByCreatedAtAsc(CallStatus status);

    /** 같은 테이블에서 중복 호출(버튼 연타) 방지 */
    boolean existsByTableIdAndStatus(Long tableId, CallStatus status);
}
