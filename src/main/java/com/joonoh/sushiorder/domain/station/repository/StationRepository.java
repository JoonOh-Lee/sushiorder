package com.joonoh.sushiorder.domain.station.repository;

import com.joonoh.sushiorder.domain.station.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findByActiveTrueOrderBySortOrderAsc();
    List<Station> findAllByOrderBySortOrderAsc();
}
