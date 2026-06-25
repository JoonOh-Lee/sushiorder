package com.joonoh.sushiorder.domain.floorplan.repository;

import com.joonoh.sushiorder.domain.floorplan.entity.FloorPlanElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FloorPlanElementRepository extends JpaRepository<FloorPlanElement, Long> {
}
