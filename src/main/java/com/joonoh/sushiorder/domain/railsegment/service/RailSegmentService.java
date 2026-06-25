package com.joonoh.sushiorder.domain.railsegment.service;

import com.joonoh.sushiorder.domain.railsegment.dto.RailSegmentResponse;
import com.joonoh.sushiorder.domain.railsegment.entity.RailSegment;
import com.joonoh.sushiorder.domain.railsegment.exception.RailSegmentNotFoundException;
import com.joonoh.sushiorder.domain.railsegment.repository.RailSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RailSegmentService {

    private final RailSegmentRepository railSegmentRepository;

    public List<RailSegmentResponse> getAllSegments() {
        return railSegmentRepository.findAllByOrderBySequenceOrderAsc().stream()
                .map(RailSegmentResponse::from)
                .toList();
    }

    @Transactional
    public RailSegmentResponse activate(Long id) {
        RailSegment segment = findSegmentOrThrow(id);
        segment.activate();
        return RailSegmentResponse.from(segment);
    }

    @Transactional
    public RailSegmentResponse deactivate(Long id) {
        RailSegment segment = findSegmentOrThrow(id);
        segment.deactivate();
        return RailSegmentResponse.from(segment);
    }

    private RailSegment findSegmentOrThrow(Long id) {
        return railSegmentRepository.findById(id)
                .orElseThrow(() -> new RailSegmentNotFoundException(id));
    }
}
