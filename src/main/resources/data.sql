-- ============================================
-- 1. 자리(Station) 시드
-- ============================================
INSERT INTO station (name, sort_order, is_active, created_at, updated_at) VALUES
                                                                              ('아부리다이', 0, true, NOW(), NOW()),
                                                                              ('육회다이', 1, true, NOW(), NOW()),
                                                                              ('참치다이', 2, true, NOW(), NOW()),
                                                                              ('활어다이', 3, true, NOW(), NOW()),
                                                                              ('뒷주방', 4, true, NOW(), NOW()),
                                                                              ('홀', 5, true, NOW(), NOW());

-- ============================================
-- 2. 메뉴(Menu) 시드
--    온다스시 파주운정점 실제 메뉴판 사진(menu1.png 레일 초밥보드, menu2.png 뒷주방
--    구이/튀김/디저트/우동 보드) 기준. 레일 초밥은 1접시 1,990원, 프리미엄은
--    1접시 3,980원 — 보드에 가격이 없는 뒷주방 메뉴(구운초밥/사이드/디저트)는
--    유사 메뉴 시세를 참고한 추정가이니 실제 운영 가격으로 추후 보정 필요.
-- ============================================

-- 프리미엄초밥 (PREMIUM_SUSHI) — 1접시 3,980원, 일부 일일 한정 수량
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('엔가와초밥', '광어 살 중에서도 제일 쫄깃한 부위만 골랐어요', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '광어 지느러미살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('연어뱃살초밥', '기름이 좔좔, 한 입이면 끝나는 맛', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '연어 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('도미뱃살초밥', '담백함과 고소함을 동시에 잡았어요', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '도미 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('오도로초밥', '참치 중 가장 귀한 부위, 오늘만 한정 수량', 1990, 'PREMIUM_SUSHI', NULL, '참다랑어 뱃살, 초밥용 밥, 와사비', '없음', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('메카도로초밥', '고소함 가득, 황새치 뱃살 한 점', 1990, 'PREMIUM_SUSHI', NULL, '황새치 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('이쿠라군함', '톡톡 터지는 연어알, 오늘만 만날 수 있어요', 1990, 'PREMIUM_SUSHI', NULL, '연어알, 초밥용 밥, 김', '난류', 8, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('안키모초밥', '바다의 푸아그라라 불리는 그 맛, 아귀간', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '아귀 간, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('우니군함', '크리미한 우니, 한정 수량이라 서둘러야 해요', 1990, 'PREMIUM_SUSHI', NULL, '성게알, 초밥용 밥, 김', '없음', 5, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('블랙타이거새우초밥', '한 점만으로 입이 꽉 차는 블랙타이거새우', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '블랙타이거새우, 초밥용 밥, 와사비', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW());

-- 신선초밥 (FRESH_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('광어초밥', '쫄깃함이 살아있는 활광어 초밥', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '광어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('연어초밥', '노르웨이에서 갓 온 신선한 연어', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '연어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('연어양파초밥', '연어 위에 양파 톡톡, 알싱한 조합', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '연어, 양파, 초밥용 밥, 마요네즈', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('참돔마스카와초밥', '껍질까지 맛있는 참돔, 마스카와로 즐겨요', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '참돔, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('활어묵은지초밥', '잘 익은 묵은지와 활어의 의외로 찰떡인 만남', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '활어, 묵은지, 초밥용 밥', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('생새우초밥', '탱글탱글 씹히는 생새우, 달큰한 감칠맛이 매력이에요', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '생새우, 초밥용 밥, 와사비', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('초새우초밥', '새콤달콤하게 무친 새우, 입맛 돋우는 한 점', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '새우, 식초, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('문어초밥', '졸깃하게 씹히는 맛이 살아있는 문어초밥', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '문어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('한치초밥', '부드럽고 쫄깃한 한치, 씹을수록 고소해요', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '한치, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('한치다리초밥', '탱글한 한치 다리만 골라 담았어요', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '한치 다리, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('갑오징어초밥', '달큰하고 쫄깃한 갑오징어 한 점', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '갑오징어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('피뿔고둥초밥', '꼬들꼬들 씹는 맛이 일품인 피뿔고둥', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '피뿔고둥, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('가리비초밥', '버터 없이도 고소한, 자연 그대로의 가리비', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '가리비, 초밥용 밥, 와사비', '조개류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('찜전복초밥', '푹 쪄서 더 부드러워진 전복 한 점', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '전복, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('감태단새우안키모초밥', '감태향 솔솔, 달콤한 단새우와 고소한 안키모를 한 번에', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '감태, 단새우, 아귀 간, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('감태단새우우니초밥', '감태로 감싼 단새우와 크리미한 우니의 만남', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '감태, 단새우, 성게알, 초밥용 밥, 김', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW());

-- 참치초밥 (TUNA_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('황새치묵은지초밥', '쫄깃한 황새치와 잘 익은 묵은지의 의외의 조합', 1990, 'TUNA_SUSHI', '/images/menu/sushi.svg', '황새치, 묵은지, 초밥용 밥', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('참치등살초밥', '깔끔하고 담백한 참치 한 점', 1990, 'TUNA_SUSHI', '/images/menu/sushi.svg', '참치, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW());

-- 고기초밥 (MEAT_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('육회초밥', '신선한 소고기 육회, 고소함이 입에 착', 1990, 'MEAT_SUSHI', '/images/menu/sushi.svg', '소고기, 초밥용 밥, 참기름, 마늘', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('연어육회말이초밥', '신선한 육회를 연어로 돌려 만 특별한 한 점', 1990, 'MEAT_SUSHI', '/images/menu/sushi.svg', '연어, 소고기 육회, 초밥용 밥, 참기름', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('와규타다끼초밥', '겉만 살짝 익힌 와규, 부드럽게 녹는 맛', 1990, 'MEAT_SUSHI', '/images/menu/sushi.svg', '와규, 초밥용 밥, 간장 소스', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW());

-- 군함초밥 (GUNKAN_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('날치알군함', '톡톡 씹히는 날치알을 가득 올렸어요', 1990, 'GUNKAN_SUSHI', '/images/menu/sushi.svg', '날치알, 초밥용 밥, 김, 마요네즈', '난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('네기도로군함', '잘게 다진 참치와 파, 어우러지는 고소한 한 입', 1990, 'GUNKAN_SUSHI', '/images/menu/sushi.svg', '참치, 파, 초밥용 밥, 김', '없음', 0, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('타코와사비군함', '와사비 향 알싱하게, 쫄깃한 타코와사비', 1990, 'GUNKAN_SUSHI', '/images/menu/sushi.svg', '문어, 와사비, 초밥용 밥, 김', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('연어양파군함', '연어와 양파의 알싱한 조합, 군함으로 즐겨요', 1990, 'GUNKAN_SUSHI', '/images/menu/sushi.svg', '연어, 양파, 마요네즈, 초밥용 밥, 김', '없음', 0, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW());

-- 구운초밥 (GRILLED_SUSHI) — 직접 구운 스테이크류.  — 1접시 1,990원.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('소불고기스테이크초밥', '달콤짭짤한 불고기 스테이크, 인기 메뉴예요', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '소고기, 양념(간장, 설탕, 마늘), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('육회타다끼초밥', '겉만 살짝, 속은 촉촉한 소고기 타다끼', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '소고기, 초밥용 밥, 간장 소스', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('관자스테이크초밥', '버터향 솔솔, 노릇하게 구운 관자', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '관자, 버터, 초밥용 밥', '조개류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('생새우갈릭스테이크초밥', '마늘버터 향 가득, 통통한 생새우 스테이크', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '생새우, 마늘, 버터, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('연어육회말이타다끼초밥', '연어로 감싼 육회를 살짝 그릴에 구워낸 타다끼', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '연어, 소고기 육회, 초밥용 밥, 간장 소스', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('와규스테이크초밥', '버터향 가득, 노릇하게 구운 와규 스테이크', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '와규, 버터, 초밥용 밥', '우유', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('도미양념구이초밥', '특제 양념 발라 노릇하게 구운 도미', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '도미, 양념(간장, 미림), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('치즈계란초밥', '고소한 치즈 한 장 올린 든든한 계란초밥', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '계란, 치즈, 초밥용 밥', '난류, 우유', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('계란새우초밥', '계란과 새우를 한 번에, 든든한 한 점', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '계란, 새우, 초밥용 밥', '난류, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('날치알치즈새우초밥', '톡톡 날치알에 고소한 치즈, 통통한 새우까지', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '날치알, 치즈, 새우, 초밥용 밥', '난류, 우유, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('베이컨새우초밥', '짭짤한 베이컨과 통통한 새우의 조합', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '베이컨, 새우, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('대하구이초밥', '통째로 구운 대하, 한 점에 만족감 가득', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '대하, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('민물장어초밥', '달콤한 소스 입혀 노릇하게 구운 민물장어', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '민물장어, 양념(간장, 미림), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('칠리치즈새우초밥', '매콤한 칠리소스와 고소한 치즈, 새우의 환상 조합', 1990, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '새우, 칠리소스, 치즈, 초밥용 밥', '새우, 갑각류, 우유', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW());

-- 양념초밥 (SEASONED_SUSHI) — 양념을 발라 구운 종류. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('계란초밥', '달콤하게 부쳐낸 폭신폭신한 계란말이 한 점', 1990, 'SEASONED_SUSHI', '/images/menu/sushi.svg', '계란, 초밥용 밥, 설탕, 미림', '난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('유부초밥', '달콤짭짤한 유부 속, 한입에 쏙 들어가는 유부초밥', 1990, 'SEASONED_SUSHI', '/images/menu/sushi.svg', '유부, 초밥용 밥, 깨', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('간장새우초밥', '짭짤한 간장소스 입은 새우, 감칠맛 한가득', 1990, 'SEASONED_SUSHI', '/images/menu/sushi.svg', '새우, 간장 소스, 초밥용 밥', '새우, 갑각류, 대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW()),
    ('양념새우초밥', '특제 양념이 매력 포인트인 새우 한 점', 1990, 'SEASONED_SUSHI', '/images/menu/sushi.svg', '새우, 특제양념, 초밥용 밥', '새우, 갑각류, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '참치다이'), 0, NOW(), NOW());

-- 튀김류 (FRIED) — 뒷주방 튀김 보드. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('새우튀김', '겉바속촉, 튀김계의 정석 새우튀김', 1990, 'FRIED', '/images/menu/side.svg', '새우, 튀김가루, 식용유', '새우, 갑각류, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('가라아게', '바삭바삭 일본식 치킨, 가라아게', 1990, 'FRIED', '/images/menu/side.svg', '닭고기, 튀김가루, 식용유', '밀, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('오징어다리튀김', '바삭하게 튀긴 오징어 다리, 한 입에 쏙', 1990, 'FRIED', '/images/menu/side.svg', '오징어 다리, 튀김가루, 식용유', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('멘보샤튀김', '새우살 가득 채운 바삭한 토스트 튀김', 1990, 'FRIED', '/images/menu/side.svg', '새우살, 식빵, 튀김가루, 식용유', '새우, 갑각류, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('왕새우튀김', '통통한 왕새우 한 마리, 겉바속촉 튀김', 1990, 'FRIED', '/images/menu/side.svg', '왕새우, 튀김가루, 식용유', '새우, 갑각류, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 식사류 (MEAL) — 우동/맑은탕 보드. 가격은 보드 표기, 미소시루는 추정가.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('왕새우우동(대)', '왕새우 한가득, 든든하게 즐기는 우동', 8000, 'MEAL', '/images/menu/side.svg', '우동면, 왕새우, 가쓰오부시 육수, 대파', '밀, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('우동(소)', '뜨끈한 국물이 생각날 때 딱, 기본 우동', 4000, 'MEAL', '/images/menu/side.svg', '우동면, 가쓰오부시 육수, 대파', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('소바(소)', '한 그릇 든든하게, 기본 소바', 4000, 'MEAL', '/images/menu/side.svg', '메밀면, 가쓰오부시 육수, 대파', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('소바(대)', '한 그릇 더 푸짐하게, 든든한 소바', 8000, 'MEAL', '/images/menu/side.svg', '메밀면, 가쓰오부시 육수, 대파', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 음료/주류 (DRINK_ALCOHOL) — 홀 담당
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('콜라', '탄산 가득, 시원하게 한 잔', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '탄산수, 액상과당, 카라멜색소, 카페인', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('콜라(제로)', '탄산 가득, 시원하게 한 잔', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '탄산수, 액상과당, 카라멜색소, 카페인', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('환타', '탄산 가득, 시원하게 한 잔', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '탄산수, 액상과당, 카라멜색소, 카페인', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('사이다', '탄산 가득, 시원하게 한 잔', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '탄산수, 액상과당, 카라멜색소, 카페인', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('생맥주300', '시원하게 한 잔, 생맥주 300cc', 3000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '맥아, 호프, 정제수', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('생맥주500', '더 시원하게, 생맥주 500cc', 5000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '맥아, 호프, 정제수', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('참이슬후레쉬', '깔끔하고 부드러운 참이슬 후레쉬', 5000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '정제수, 주정', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('처음처럼', '부드럽게 넘어가는 처음처럼', 5000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '정제수, 주정', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('참이슬오리지널', '깊은 맛의 참이슬 오리지널', 5000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '정제수, 주정', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('테라(병)', '청량감 가득한 테라', 5000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '맥아, 호프, 정제수', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('카스(병)', '시원하게 한 병, 카스', 5000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '맥아, 호프, 정제수', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('켈리(병)', '묵직한 풍미의 켈리', 5000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '맥아, 호프, 정제수', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('뽀로로밀크', '아이들이 좋아하는 달콤한 뽀로로밀크', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '우유, 설탕', '우유', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('티니핑딸기', '상큼한 딸기맛 티니핑 음료', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '우유, 딸기맛 시럽, 설탕', '우유', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('티니핑복숭아', '달콤한 복숭아맛 티니핑 음료', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '우유, 복숭아맛 시럽, 설탕', '우유', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('티니핑포도', '진한 포도맛 티니핑 음료', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '우유, 포도맛 시럽, 설탕', '우유', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW());

-- 디저트/기타 (DESSERT_ETC) — 케이크/푸딩/아이스크림은 홀 담당, 닭꼬치는 뒷주방
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('딸기케이크', '상큼한 생딸기를 듬뿍 올렸어요', 1990, 'DESSERT_ETC', '/images/menu/dessert.svg', '빵, 생크림, 딸기, 우유', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('초코케이크', '진한 초콜릿에 풍덩, 달콤한 마무리', 1990, 'DESSERT_ETC', '/images/menu/dessert.svg', '빵, 초콜릿, 생크림, 우유', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('티라미수케이크', '은은한 커피향, 정통 이탈리안 디저트', 1990, 'DESSERT_ETC', '/images/menu/dessert.svg', '마스카포네, 커피, 코코아파우더, 빵', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('마카롱', '바삭 쫀득, 한입에 행복해지는 마카롱', 1990, 'DESSERT_ETC', '/images/menu/dessert.svg', '아몬드가루, 설탕, 난백, 버터크림', '난류, 우유, 견과류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('바닐라아이스크림', '부드럽게 녹는 바닐라 아이스크림', 1990, 'DESSERT_ETC', '/images/menu/dessert.svg', '우유, 생크림, 바닐라빈, 설탕', '우유', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('초코아이스크림', '진하게 즐기는 초코 아이스크림', 1990, 'DESSERT_ETC', '/images/menu/dessert.svg', '우유, 생크림, 코코아파우더, 설탕', '우유', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '홀'), 0, NOW(), NOW()),
    ('닭꼬치', '달콤짭짤한 양념 입혀 구운 닭꼬치', 1990, 'DESSERT_ETC', '/images/menu/side.svg', '닭고기, 양념(간장, 설탕), 대파', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 포장 (TAKEOUT) — 온다스시 포장 메뉴판(pojang_menu.png) 기준. 세트는 묶음 구성을 description에 표기.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('온다초밥(10P)', '혼자 먹어도 든든한 모둠 10피스 — 활어2P+연어양파1P+연어2P+초새우1P+소고기직화1P+계란새우1P+구운새우1P+간장새우1P', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 계란, 초밥용 밥', '새우, 갑각류, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('온다스페셜초밥(12P)', '한 단계 업그레이드한 스페셜 12피스 — 활어3P+연어양파1P+연어2P+육회타다끼1P+소고기직화1P+육회초밥1P+구운새우1P+간장새우1P+계란새우1P', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 육회, 계란, 초밥용 밥', '새우, 갑각류, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('온다패밀리초밥세트(29P+우동(대)+새우튀김)', '다 같이 먹기 딱 좋은 패밀리세트 — 활어6P+연어4P+연어양파2P+육회타다끼2P+소고기직화2P+육회초밥2P+구운새우2P+간장새우2P+계란새우2P+유부초밥5P+우동(대)+새우튀김', 39900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 육회, 계란, 유부, 우동면, 초밥용 밥', '새우, 갑각류, 난류, 밀, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('연어초밥(10p)포장', '포장으로도 신선하게, 연어초밥 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('연어양파초밥(10p)포장', '양파 토핑까지 챙긴 연어양파초밥 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 양파, 초밥용 밥, 마요네즈', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('광어초밥(10p)포장', '포장으로 즐기는 쫄깃한 광어초밥 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '광어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('광어묵은지초밥(10p)포장', '광어와 묵은지의 색다른 조합 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '광어, 묵은지, 초밥용 밥', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('소고기직화초밥(10p)포장', '불맛 가득 직화 소고기 10피스', 13900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 양념(간장, 설탕, 마늘), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('연어직화초밥(10p)포장', '불맛 입힌 직화 연어 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 초밥용 밥, 간장 소스', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('생새우마늘직화초밥(10p)포장', '마늘향 솔솔, 직화 생새우 10피스', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '생새우, 마늘, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회타다끼초밥(10p)포장', '겉만 살짝 익힌 소고기 타다끼 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 간장 소스', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('구운새우초밥(10p)포장', '노릇하게 구운 새우 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('계란새우초밥(10p)포장', '계란과 새우 듀오, 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '계란, 새우, 초밥용 밥', '난류, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회초밥(5p)+육회타다끼(5p)포장', '육회 두 가지 맛을 한 번에, 모둠 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 참기름, 마늘', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('양념새우초밥(10p)포장', '특제 양념이 매력 포인트, 새우 10피스', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 특제양념, 초밥용 밥', '새우, 갑각류, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회초밥(10p)포장', '포장으로도 신선하게, 육회초밥 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 참기름, 마늘', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('간장새우초밥(10p)포장', '짭짤한 간장소스 새우 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 간장 소스, 초밥용 밥', '새우, 갑각류, 대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW());

-- ============================================
-- 3. 직원(Staff) 시드 — 로컬 개발용 계정
--    admin / admin1234 (ROLE_ADMIN, station 없음)
--    station별 직원 5명 / staff1234 (ROLE_STAFF) — station은 출근 후 본인이 재배정 가능
-- ============================================
INSERT INTO staff (username, password, role, station_id, created_at, updated_at) VALUES
    ('admin', '$2a$10$dkfi6Vcq77YzbrGX42Cvd.LVr4OzdHl0ijKnFIsg0OYcFkH79mZGG', 'ADMIN', NULL, NOW(), NOW()),
    ('staff_aburi', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '아부리다이'), NOW(), NOW()),
    ('staff_yukhwe', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '육회다이'), NOW(), NOW()),
    ('staff_tuna', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '참치다이'), NOW(), NOW()),
    ('staff_live', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '활어다이'), NOW(), NOW()),
    ('staff_back', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '뒷주방'), NOW(), NOW());

-- ============================================
-- 4. 손님 좌석(RestaurantTable) 시드
--    COUNTER(다찌석, 1인용) 1~20번 / TABLE(테이블, 4인용) 1~3번
--    table_number는 seat_type별로 따로 매겨진다 (seat_type+table_number 복합 unique)
--    x/y/width/height는 매장 평면도용 좌표(0~100 퍼센트). 가운데 주방+레일(floor_plan_element
--    시드 참고)을 한 바퀴 도는
--    루프 형태로 배치 — 왼쪽 세로줄(다찌석 1→10, 아래→위), 위쪽 가로줄(테이블 1→3),
--    오른쪽 세로줄(다찌석 11→20, 위→아래), 아래쪽은 입구/통로라 비워둠.
-- ============================================
INSERT INTO restaurant_table (seat_type, table_number, seat_count, status, x, y, width, height, created_at, updated_at) VALUES
    ('COUNTER', 1, 1, 'EMPTY', 5, 91, 15, 5, NOW(), NOW()),
    ('COUNTER', 2, 1, 'EMPTY', 5, 82, 15, 5, NOW(), NOW()),
    ('COUNTER', 3, 1, 'EMPTY', 5, 73, 15, 5, NOW(), NOW()),
    ('COUNTER', 4, 1, 'EMPTY', 5, 64, 15, 5, NOW(), NOW()),
    ('COUNTER', 5, 1, 'EMPTY', 5, 55, 15, 5, NOW(), NOW()),
    ('COUNTER', 6, 1, 'EMPTY', 5, 46, 15, 5, NOW(), NOW()),
    ('COUNTER', 7, 1, 'EMPTY', 5, 37, 15, 5, NOW(), NOW()),
    ('COUNTER', 8, 1, 'EMPTY', 5, 28, 15, 5, NOW(), NOW()),
    ('COUNTER', 9, 1, 'EMPTY', 5, 19, 15, 5, NOW(), NOW()),
    ('COUNTER', 10, 1, 'EMPTY', 5, 10, 15, 5, NOW(), NOW()),
    ('COUNTER', 11, 1, 'EMPTY', 80, 10, 15, 5, NOW(), NOW()),
    ('COUNTER', 12, 1, 'EMPTY', 80, 19, 15, 5, NOW(), NOW()),
    ('COUNTER', 13, 1, 'EMPTY', 80, 28, 15, 5, NOW(), NOW()),
    ('COUNTER', 14, 1, 'EMPTY', 80, 37, 15, 5, NOW(), NOW()),
    ('COUNTER', 15, 1, 'EMPTY', 80, 46, 15, 5, NOW(), NOW()),
    ('COUNTER', 16, 1, 'EMPTY', 80, 55, 15, 5, NOW(), NOW()),
    ('COUNTER', 17, 1, 'EMPTY', 80, 64, 15, 5, NOW(), NOW()),
    ('COUNTER', 18, 1, 'EMPTY', 80, 73, 15, 5, NOW(), NOW()),
    ('COUNTER', 19, 1, 'EMPTY', 80, 82, 15, 5, NOW(), NOW()),
    ('COUNTER', 20, 1, 'EMPTY', 80, 91, 15, 5, NOW(), NOW()),
    ('TABLE', 1, 4, 'EMPTY', 30, 0, 15, 5, NOW(), NOW()),
    ('TABLE', 2, 4, 'EMPTY', 45, 0, 15, 5, NOW(), NOW()),
    ('TABLE', 3, 4, 'EMPTY', 60, 0, 15, 5, NOW(), NOW());

-- ============================================
-- 4-1. 매장 평면도 고정 시설(FloorPlanElement) 시드
--    주방만 박스로 표현 — 레일은 더 이상 박스가 아니라 rail_segment 목록(아래 4-2)으로 표현한다.
-- ============================================
INSERT INTO floor_plan_element (type, label, x, y, width, height, created_at, updated_at) VALUES
    ('KITCHEN', '주방', 40, 13, 25, 65, NOW(), NOW());
-- ============================================
-- 4-2. 회전초밥 레일 구간(RailSegment) 시드
--    좌석 사이를 잇는 구간 단위로 표현 — x/y는 안 저장하고 from/toTableId만 참조,
--    실제 선 좌표는 프론트가 RestaurantTable의 x/y로 매번 계산한다.
--    다찌석1→...→다찌석10→테이블1→테이블2→테이블3→다찌석11→...→다찌석20으로 끝나는
--    경로. 다찌석1과 다찌석20은 평면도상 마주보는 게 아니라 입구/통로(아래쪽 빈 공간)를
--    사이에 두고 멀찍이 떨어져 있어서 둘을 잇는 구간은 없다(루프가 닫히지 않음).
--    손님이 적을 때 일부 구간을 꺼서(active=false) 순환 범위를 줄이는 용도라
--    시드는 전부 active=true(전체 구간 켜짐)로 시작한다.
-- ============================================
INSERT INTO rail_segment (sequence_order, from_table_id, to_table_id, active, created_at, updated_at) VALUES
    (1, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 1),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 2), true, NOW(), NOW()),
    (2, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 2),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 3), true, NOW(), NOW()),
    (3, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 3),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 4), true, NOW(), NOW()),
    (4, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 4),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 5), true, NOW(), NOW()),
    (5, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 5),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 6), true, NOW(), NOW()),
    (6, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 6),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 7), true, NOW(), NOW()),
    (7, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 7),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 8), true, NOW(), NOW()),
    (8, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 8),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 9), true, NOW(), NOW()),
    (9, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 9),
        (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 10), true, NOW(), NOW()),
    (10, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 10),
         (SELECT id FROM restaurant_table WHERE seat_type = 'TABLE' AND table_number = 1), true, NOW(), NOW()),
    (11, (SELECT id FROM restaurant_table WHERE seat_type = 'TABLE' AND table_number = 1),
         (SELECT id FROM restaurant_table WHERE seat_type = 'TABLE' AND table_number = 2), true, NOW(), NOW()),
    (12, (SELECT id FROM restaurant_table WHERE seat_type = 'TABLE' AND table_number = 2),
         (SELECT id FROM restaurant_table WHERE seat_type = 'TABLE' AND table_number = 3), true, NOW(), NOW()),
    (13, (SELECT id FROM restaurant_table WHERE seat_type = 'TABLE' AND table_number = 3),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 11), true, NOW(), NOW()),
    (14, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 11),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 12), true, NOW(), NOW()),
    (15, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 12),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 13), true, NOW(), NOW()),
    (16, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 13),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 14), true, NOW(), NOW()),
    (17, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 14),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 15), true, NOW(), NOW()),
    (18, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 15),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 16), true, NOW(), NOW()),
    (19, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 16),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 17), true, NOW(), NOW()),
    (20, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 17),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 18), true, NOW(), NOW()),
    (21, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 18),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 19), true, NOW(), NOW()),
    (22, (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 19),
         (SELECT id FROM restaurant_table WHERE seat_type = 'COUNTER' AND table_number = 20), true, NOW(), NOW());

-- ============================================
-- 5. 공지(Notice) 시드
-- ============================================
INSERT INTO notice (title, content, pinned, is_active, created_at, updated_at) VALUES
    ('영업시간 꼭 확인하세요 ⏰', '매일 11:30~22:00까지 영업해요 (라스트오더 21:20!) 매달 넷째 주 수요일은 쉬어가는 날이니 참고해주세요 🙏', true, true, NOW(), NOW()),
    ('프리미엄 메뉴, 빨리 픽하세요', '오도로·이쿠라·우니 같은 프리미엄 초밥은 매일 한정 수량만 준비돼요. 소진되면 바로 다음 날 만나요!', false, true, NOW(), NOW()),
    ('오픈 이벤트가 마무리됐어요', '오픈 기념 할인 이벤트는 종료됐어요. 그동안 찾아주셔서 정말 감사해요 :)', false, false, NOW(), NOW());
