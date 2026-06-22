package com.joonoh.sushiorder.domain.session.scheduler;

import com.joonoh.sushiorder.domain.session.service.TableSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionExpirationScheduler {

    private final TableSessionService tableSessionService;

    @Scheduled(fixedRate = 60_000)
    public void expireOverdueSessions() {
        tableSessionService.expireOverdueSessions();
    }
}
