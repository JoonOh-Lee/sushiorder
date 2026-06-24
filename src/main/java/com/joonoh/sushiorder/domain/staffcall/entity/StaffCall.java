package com.joonoh.sushiorder.domain.staffcall.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "staff_call")
public class StaffCall extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aggregate 간 참조는 ID로만 — RestaurantTable/TableSession과는 별개 Aggregate
    @Column(name = "table_id", nullable = false)
    private Long tableId;
    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CallType type;

    // ITEM_REQUEST(물품 요청)일 때만 사용 — 장국추가/물티슈/수저젓가락/락교초생강 등 요청 품목명
    @Column(name = "item_name", length = 50)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CallStatus status;

    @Builder
    private StaffCall(Long tableId, Long sessionId, CallType type, String itemName) {
        if (tableId == null) {
            throw new IllegalArgumentException("tableId는 필수입니다.");
        }
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId는 필수입니다.");
        }
        if (type == null) {
            throw new IllegalArgumentException("호출 유형은 필수입니다.");
        }
        if (type == CallType.ITEM_REQUEST && (itemName == null || itemName.isBlank())) {
            throw new IllegalArgumentException("요청할 물품명은 필수입니다.");
        }
        this.tableId = tableId;
        this.sessionId = sessionId;
        this.type = type;
        this.itemName = type == CallType.ITEM_REQUEST ? itemName : null;
        this.status = CallStatus.REQUESTED;
    }

    /** 직원이 처리 완료로 표시 */
    public void resolve() {
        if (this.status == CallStatus.RESOLVED) {
            throw new IllegalStateException("이미 처리된 호출입니다.");
        }
        this.status = CallStatus.RESOLVED;
    }
}
