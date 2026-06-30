package com.joonoh.sushiorder.domain.staff.service;

import com.joonoh.sushiorder.domain.auditlog.entity.AuditAction;
import com.joonoh.sushiorder.domain.staff.dto.*;
import com.joonoh.sushiorder.domain.staff.entity.Staff;
import com.joonoh.sushiorder.domain.staff.entity.StaffRole;
import com.joonoh.sushiorder.domain.staff.exception.InvalidCredentialsException;
import com.joonoh.sushiorder.domain.staff.exception.StaffNotFoundException;
import com.joonoh.sushiorder.domain.staff.repository.StaffRepository;
import com.joonoh.sushiorder.domain.station.exception.StationNotFoundException;
import com.joonoh.sushiorder.domain.station.repository.StationRepository;
import com.joonoh.sushiorder.global.audit.Audit;
import com.joonoh.sushiorder.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffService {

    private final StaffRepository staffRepository;
    private final StationRepository stationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Audit(action = AuditAction.STAFF_LOGIN, entityType = "STAFF")
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Staff staff = staffRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        if (!staff.isActive()) {
            throw new InvalidCredentialsException("비활성화된 계정입니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), staff.getPassword())) {
            throw new InvalidCredentialsException();
        }

        staff.resetDuty();
        staff.assignStation(null); // 로그인마다 스테이션 재선택 강제

        String token = jwtTokenProvider.createToken(staff.getUsername(), staff.getRole());

        return LoginResponse.builder()
                .token(token)
                .username(staff.getUsername())
                .role(staff.getRole())
                .stationId(staff.getStationId())
                .onDuty(false)
                .build();
    }

    public StaffMeResponse getMe(String username) {
        return StaffMeResponse.from(findStaffOrThrow(username));
    }

    /** 출근할 때마다 바뀔 수 있는 자리를 직원 본인이 로그인 후 직접 지정 */
    @Transactional
    public StaffMeResponse assignStation(String username, Long stationId) {
        Staff staff = findStaffOrThrow(username);

        if (!stationRepository.existsById(stationId)) {
            throw new StationNotFoundException(stationId);
        }

        staff.assignStation(stationId);
        return StaffMeResponse.from(staff);
    }

    @Transactional
    public StaffMeResponse setDuty(String username, boolean on) {
        Staff staff = findStaffOrThrow(username);
        if (on) {
            if (staff.getStationId() == null) {
                throw new IllegalArgumentException("스테이션을 먼저 선택해주세요.");
            }
            if (staffRepository.existsByStationIdAndOnDutyTrueAndIdNot(staff.getStationId(), staff.getId())) {
                throw new IllegalArgumentException("이미 다른 직원이 해당 스테이션에서 근무 중입니다.");
            }
            staff.startDuty();
        } else {
            staff.endDuty();
        }
        return StaffMeResponse.from(staff);
    }

    // ===== Admin 전용 =====

    public List<StaffResponse> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(StaffResponse::from)
                .toList();
    }

    @Transactional
    public StaffResponse createStaff(StaffCreateRequest request) {
        if (staffRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        Staff staff = Staff.builder()
                .username(request.getUsername())
                .encodedPassword(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        return StaffResponse.from(staffRepository.save(staff));
    }

    @Transactional
    public void changeRole(Long id, StaffRole role) {
        findStaffOrThrow(id).changeRole(role);
    }

    @Transactional
    public void changePassword(Long id, String rawPassword) {
        findStaffOrThrow(id).changePassword(passwordEncoder.encode(rawPassword));
    }

    @Transactional
    public void deactivate(Long id, String requesterUsername) {
        Staff requester = findStaffOrThrow(requesterUsername);
        if (requester.getId().equals(id)) {
            throw new IllegalArgumentException("본인 계정은 비활성화할 수 없습니다.");
        }
        findStaffOrThrow(id).deactivate();
    }

    @Transactional
    public void activate(Long id) {
        findStaffOrThrow(id).activate();
    }

    private Staff findStaffOrThrow(String username) {
        return staffRepository.findByUsername(username)
                .orElseThrow(() -> new StaffNotFoundException(username));
    }

    private Staff findStaffOrThrow(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));
    }
}
