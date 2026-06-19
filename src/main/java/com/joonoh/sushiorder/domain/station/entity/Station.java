package com.joonoh.sushiorder.domain.station.entity;

import com.joonoh.sushiorder.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "station")
public class Station extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 30)
    private String name;
    @Column(name ="sort_order", nullable = false)
    private int sortOrder;
    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Builder
    public Station(Long id, String name, int sortOrder) {
        this.id = id;
        this.name = name;
        this.sortOrder = sortOrder;
        this.active = true;
    }

    public void rename(String newName) {
        this.name = newName;
    }

    public void changeOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
