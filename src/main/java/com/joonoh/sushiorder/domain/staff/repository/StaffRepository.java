package com.joonoh.sushiorder.domain.staff.repository;

import com.joonoh.sushiorder.domain.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByStationIdAndOnDutyTrue(Long stationId);

    boolean existsByStationIdAndOnDutyTrueAndIdNot(Long stationId, Long id);
}
