package com.joonoh.sushiorder.domain.notice.service;

import com.joonoh.sushiorder.domain.notice.dto.NoticeCreateRequest;
import com.joonoh.sushiorder.domain.notice.dto.NoticeResponse;
import com.joonoh.sushiorder.domain.notice.dto.NoticeUpdateRequest;
import com.joonoh.sushiorder.domain.notice.exception.NoticeNotFoundException;
import com.joonoh.sushiorder.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class NoticeServiceTest {

    @Autowired private NoticeService noticeService;
    @Autowired private NoticeRepository noticeRepository;

    private Long noticeId;

    @AfterEach
    void tearDown() {
        if (noticeId != null) {
            noticeRepository.deleteById(noticeId);
            noticeId = null;
        }
    }

    @Test
    @DisplayName("공지를 생성하면 활성/고정 해제 상태로 시작한다")
    void createNotice_startsActiveAndUnpinned() {
        NoticeResponse response = noticeService.createNotice(createRequest("점검 안내", "내일 점검합니다."));
        noticeId = response.getId();

        assertThat(response.isActive()).isTrue();
        assertThat(response.isPinned()).isFalse();
    }

    @Test
    @DisplayName("비활성 공지는 손님/직원용 목록에서 빠진다")
    void getActiveNotices_excludesDeactivatedNotice() {
        NoticeResponse response = noticeService.createNotice(createRequest("휴무 안내", "금요일 휴무입니다."));
        noticeId = response.getId();

        noticeService.deactivateNotice(noticeId);

        assertThat(noticeService.getActiveNotices())
                .extracting(NoticeResponse::getId)
                .doesNotContain(noticeId);
        assertThat(noticeService.getAllNotices())
                .extracting(NoticeResponse::getId)
                .contains(noticeId);
    }

    @Test
    @DisplayName("고정한 공지가 활성 목록 맨 앞에 온다")
    void pinNotice_appearsFirstInActiveList() {
        NoticeResponse other = noticeService.createNotice(createRequest("일반 공지", "내용"));
        NoticeResponse pinned = noticeService.createNotice(createRequest("고정 공지", "내용"));
        noticeService.pinNotice(pinned.getId());

        try {
            assertThat(noticeService.getActiveNotices().get(0).getId()).isEqualTo(pinned.getId());
        } finally {
            noticeRepository.deleteById(other.getId());
            noticeId = pinned.getId();
        }
    }

    @Test
    @DisplayName("공지를 수정하면 제목/내용이 바뀐다")
    void updateNotice_changesTitleAndContent() {
        NoticeResponse response = noticeService.createNotice(createRequest("원래 제목", "원래 내용"));
        noticeId = response.getId();

        noticeService.updateNotice(noticeId, updateRequest("새 제목", "새 내용"));

        NoticeResponse updated = noticeService.getAllNotices().stream()
                .filter(n -> n.getId().equals(noticeId))
                .findFirst().orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("새 제목");
        assertThat(updated.getContent()).isEqualTo("새 내용");
    }

    @Test
    @DisplayName("존재하지 않는 공지는 수정할 수 없다")
    void updateNotice_notFound_throws() {
        assertThatThrownBy(() -> noticeService.updateNotice(999_999L, updateRequest("제목", "내용")))
                .isInstanceOf(NoticeNotFoundException.class);
    }

    private NoticeCreateRequest createRequest(String title, String content) {
        NoticeCreateRequest request = new NoticeCreateRequest();
        setField(request, "title", title);
        setField(request, "content", content);
        return request;
    }

    private NoticeUpdateRequest updateRequest(String title, String content) {
        NoticeUpdateRequest request = new NoticeUpdateRequest();
        setField(request, "title", title);
        setField(request, "content", content);
        return request;
    }

    private void setField(Object target, String name, Object value) {
        try {
            var field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
