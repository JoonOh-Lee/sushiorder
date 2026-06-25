package com.joonoh.sushiorder.domain.floorplan.service;

import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementCreateRequest;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementPositionRequest;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementResponse;
import com.joonoh.sushiorder.domain.floorplan.dto.FloorPlanElementUpdateRequest;
import com.joonoh.sushiorder.domain.floorplan.entity.FloorPlanElement;
import com.joonoh.sushiorder.domain.floorplan.exception.FloorPlanElementNotFoundException;
import com.joonoh.sushiorder.domain.floorplan.repository.FloorPlanElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FloorPlanElementService {

    private final FloorPlanElementRepository floorPlanElementRepository;

    @Transactional
    public FloorPlanElementResponse createElement(FloorPlanElementCreateRequest request) {
        FloorPlanElement element = FloorPlanElement.builder()
                .type(request.getType())
                .label(request.getLabel())
                .x(request.getX())
                .y(request.getY())
                .width(request.getWidth())
                .height(request.getHeight())
                .build();

        return FloorPlanElementResponse.from(floorPlanElementRepository.save(element));
    }

    public List<FloorPlanElementResponse> getAllElements() {
        return floorPlanElementRepository.findAll().stream()
                .map(FloorPlanElementResponse::from)
                .toList();
    }

    public FloorPlanElementResponse getElement(Long id) {
        return FloorPlanElementResponse.from(findElementOrThrow(id));
    }

    @Transactional
    public FloorPlanElementResponse updatePosition(Long id, FloorPlanElementPositionRequest request) {
        FloorPlanElement element = findElementOrThrow(id);
        element.updatePosition(request.getX(), request.getY(), request.getWidth(), request.getHeight());
        return FloorPlanElementResponse.from(element);
    }

    @Transactional
    public FloorPlanElementResponse updateInfo(Long id, FloorPlanElementUpdateRequest request) {
        FloorPlanElement element = findElementOrThrow(id);
        element.updateInfo(request.getType(), request.getLabel());
        return FloorPlanElementResponse.from(element);
    }

    @Transactional
    public void deleteElement(Long id) {
        floorPlanElementRepository.delete(findElementOrThrow(id));
    }

    private FloorPlanElement findElementOrThrow(Long id) {
        return floorPlanElementRepository.findById(id)
                .orElseThrow(() -> new FloorPlanElementNotFoundException(id));
    }
}
