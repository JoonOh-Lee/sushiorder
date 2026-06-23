package com.joonoh.sushiorder.domain.notice.repository;

import com.joonoh.sushiorder.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByActiveTrueOrderByPinnedDescCreatedAtDesc();
    List<Notice> findAllByOrderByPinnedDescCreatedAtDesc();
}
