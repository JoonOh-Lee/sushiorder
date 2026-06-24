package com.joonoh.sushiorder.domain.notice.service;

import com.joonoh.sushiorder.domain.notice.dto.NoticeCreateRequest;
import com.joonoh.sushiorder.domain.notice.dto.NoticeResponse;
import com.joonoh.sushiorder.domain.notice.dto.NoticeUpdateRequest;
import com.joonoh.sushiorder.domain.notice.entity.Notice;
import com.joonoh.sushiorder.domain.notice.exception.NoticeNotFoundException;
import com.joonoh.sushiorder.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    /** 게시 중인 공지만 — 손님/직원 조회용, 고정 공지가 먼저 보이고 그 안에서는 최신순 */
    public List<NoticeResponse> getActiveNotices() {
        return noticeRepository.findByActiveTrueOrderByPinnedDescCreatedAtDesc().stream()
                .map(NoticeResponse::from)
                .toList();
    }

    /** 비활성 공지까지 포함 — 관리자 목록용 */
    public List<NoticeResponse> getAllNotices() {
        return noticeRepository.findAllByOrderByPinnedDescCreatedAtDesc().stream()
                .map(NoticeResponse::from)
                .toList();
    }

    @Transactional
    public NoticeResponse createNotice(NoticeCreateRequest request) {
        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return NoticeResponse.from(noticeRepository.save(notice));
    }

    @Transactional
    public void updateNotice(Long id, NoticeUpdateRequest request) {
        findNoticeOrThrow(id).update(request.getTitle(), request.getContent());
    }

    @Transactional
    public void pinNotice(Long id) {
        findNoticeOrThrow(id).pin();
    }

    @Transactional
    public void unpinNotice(Long id) {
        findNoticeOrThrow(id).unpin();
    }

    @Transactional
    public void activateNotice(Long id) {
        findNoticeOrThrow(id).activate();
    }

    @Transactional
    public void deactivateNotice(Long id) {
        findNoticeOrThrow(id).deactivate();
    }

    private Notice findNoticeOrThrow(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException(id));
    }
}
