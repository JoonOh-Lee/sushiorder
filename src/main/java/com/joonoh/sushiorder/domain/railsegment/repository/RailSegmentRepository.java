package com.joonoh.sushiorder.domain.railsegment.repository;

import com.joonoh.sushiorder.domain.railsegment.entity.RailSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RailSegmentRepository extends JpaRepository<RailSegment, Long> {

    List<RailSegment> findAllByOrderBySequenceOrderAsc();
}
