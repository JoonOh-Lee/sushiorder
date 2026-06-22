package com.joonoh.sushiorder.domain.station.service;

import com.joonoh.sushiorder.domain.station.dto.*;
import com.joonoh.sushiorder.domain.station.entity.Station;
import com.joonoh.sushiorder.domain.station.exception.StationNotFoundException;
import com.joonoh.sushiorder.domain.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public List<StationResponse> getActiveStations() {
        return stationRepository.findByActiveTrueOrderBySortOrderAsc() // List<Station>
                .stream() // Stream<Station>
                .map(StationResponse::from) // Stream<StationResponse>
                .toList(); //List<StationResponse>
    }

    public List<StationResponse> getAllStations() {
        return stationRepository.findAllByOrderBySortOrderAsc() // List<Station>
                .stream() // Stream<Station>
                .map(StationResponse::from) // Stream<StationResponse>
                .toList(); //List<StationResponse>
    }

    @Transactional
    public StationResponse createStation(StationCreateRequest request) {
        int nextOrder = stationRepository.findAllByOrderBySortOrderAsc().size();
        Station station = Station.builder()
                .name(request.getName())
                .sortOrder(nextOrder)
                .build();
        return StationResponse.from(stationRepository.save(station));
    }

    @Transactional
    public void renameStation(Long id, StationRenameRequest request) {
        findStationOrThrow(id).rename(request.getName());
    }

    private Station findStationOrThrow(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }
    
    @Transactional
    public void reorderStations(StationReorderRequest request) {
        for (StationOrderItem item : request.getOrders()) {
            findStationOrThrow(item.getStationId()).changeOrder(item.getSortOrder());
        }
        // 영속 상태 엔티티들 - 트랜잭션 커밋 시 dirty checking으로 일괄 업데이트
    }

    @Transactional
    public void activateStation(Long id) {
        findStationOrThrow(id).activate();
    }

    @Transactional
    public void deactivateStation(Long id) {
        findStationOrThrow(id).deactivate();
    }

}
