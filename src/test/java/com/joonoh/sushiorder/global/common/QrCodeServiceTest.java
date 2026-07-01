package com.joonoh.sushiorder.global.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QrCodeServiceTest {

    private static final byte[] PNG_SIGNATURE =
            {(byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n'};

    @Autowired private QrCodeService qrCodeService;

    @Test
    @DisplayName("테이블 QR을 생성하면 PNG 이미지 바이트가 나온다")
    void generateTableQr_returnsPngBytes() {
        byte[] png = qrCodeService.generateTableQr(1L);

        assertThat(png).isNotEmpty();
        assertThat(java.util.Arrays.copyOfRange(png, 0, PNG_SIGNATURE.length)).isEqualTo(PNG_SIGNATURE);
    }

    @Test
    @DisplayName("테이블 id가 다르면 인코딩되는 QR 데이터도 달라 이미지 바이트가 달라진다")
    void generateTableQr_differentTableIds_produceDifferentImages() {
        byte[] first = qrCodeService.generateTableQr(1L);
        byte[] second = qrCodeService.generateTableQr(2L);

        assertThat(first).isNotEqualTo(second);
    }
}
