-- ============================================
-- 1. 자리(Station) 시드
-- ============================================
INSERT INTO station (name, sort_order, is_active, created_at, updated_at) VALUES
                                                                              ('아부리다이', 0, true, NOW(), NOW()),
                                                                              ('육회다이', 1, true, NOW(), NOW()),
                                                                              ('연어다이', 2, true, NOW(), NOW()),
                                                                              ('활어다이', 3, true, NOW(), NOW()),
                                                                              ('뒷주방', 4, true, NOW(), NOW());

-- ============================================
-- 2. 메뉴(Menu) 시드
--    회전초밥집 메뉴판을 참고한 샘플 데이터
--    구이/튀김/디저트/우동 보드) 기준. 레일 초밥은 1접시 1,990원, 프리미엄은
--    1접시 3,980원 — 보드에 가격이 없는 뒷주방 메뉴(구운초밥/사이드/디저트)는
--    유사 메뉴 시세를 참고한 추정가이니 실제 운영 가격으로 추후 보정 필요.
-- ============================================

-- 프리미엄초밥 (PREMIUM_SUSHI) — 1접시 3,980원, 일부 일일 한정 수량
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('엔가와초밥', '광어 지느러미살', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '광어 지느러미살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('연어뱃살초밥', '기름진 연어 뱃살', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '연어 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('도미뱃살초밥', '담백하고 기름진 도미 뱃살', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '도미 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('오도로초밥', '참다랑어 뱃살 — 한정 수량', 1990, 'PREMIUM_SUSHI', NULL, '참다랑어 뱃살, 초밥용 밥, 와사비', '없음', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('메가도로초밥', '황새치 뱃살', 1990, 'PREMIUM_SUSHI', NULL, '황새치 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('이쿠라군함', '연어알 군함초밥 — 한정 수량', 1990, 'PREMIUM_SUSHI', NULL, '연어알, 초밥용 밥, 김', '난류', 8, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('안키모초밥', '아귀 간', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '아귀 간, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('우니군함', '성게알 군함초밥 — 한정 수량', 1990, 'PREMIUM_SUSHI', NULL, '성게알, 초밥용 밥, 김', '없음', 5, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('블랙타이거새우초밥', '큼직한 블랙타이거새우', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '블랙타이거새우, 초밥용 밥, 와사비', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW());

-- 신선초밥 (FRESH_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('광어초밥', '쫄깃한 식감의 활어 광어', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '광어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('연어초밥', '신선한 노르웨이산 연어', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '연어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('연어양파초밥', '연어와 양파 토핑', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '연어, 양파, 초밥용 밥, 마요네즈', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('참돔마스카와초밥', '참돔 껍질을 살린 마스카와', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '참돔, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('활어묵은지초밥', '활어와 묵은지', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '활어, 묵은지, 초밥용 밥', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW());

-- 참치초밥 (TUNA_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('참치초밥', '담백한 참치 살', 1990, 'TUNA_SUSHI', '/images/menu/sushi.svg', '참치, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW());

-- 고기초밥 (MEAT_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('육회초밥', '신선한 소고기 육회', 1990, 'MEAT_SUSHI', '/images/menu/sushi.svg', '소고기, 초밥용 밥, 참기름, 마늘', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW());

-- 군함초밥 (GUNKAN_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('날치알군함', '바삭한 날치알 군함초밥', 1990, 'GUNKAN_SUSHI', '/images/menu/sushi.svg', '날치알, 초밥용 밥, 김, 마요네즈', '난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW());

-- 구운초밥 (GRILLED_SUSHI) — 직접 구운 스테이크류. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('소불고기스테이크초밥', '달콤짭짤한 소불고기 스테이크', 3500, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '소고기, 양념(간장, 설탕, 마늘), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('육회타다끼초밥', '살짝 겉만 익힌 소고기 타다끼', 3500, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '소고기, 초밥용 밥, 간장 소스', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('관자스테이크초밥', '버터에 구운 관자', 4000, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '관자, 버터, 초밥용 밥', '조개류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('생새우갈릭스테이크초밥', '마늘버터로 구운 생새우', 4000, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '생새우, 마늘, 버터, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 양념초밥 (SEASONED_SUSHI) — 양념을 발라 구운 종류. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('도미양념구이초밥', '양념을 발라 구운 도미', 3500, 'SEASONED_SUSHI', '/images/menu/sushi.svg', '도미, 양념(간장, 미림), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 튀김류 (FRIED) — 뒷주방 튀김 보드. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('새우튀김', '바삭한 새우튀김', 3500, 'FRIED', '/images/menu/side.svg', '새우, 튀김가루, 식용유', '새우, 갑각류, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('가라아게', '바삭한 일본식 닭튀김', 3500, 'FRIED', '/images/menu/side.svg', '닭고기, 튀김가루, 식용유', '밀, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 식사류 (MEAL) — 우동/맑은탕 보드. 가격은 보드 표기, 미소시루는 추정가.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('미소시루', '따뜻한 일본 된장국', 2000, 'MEAL', '/images/menu/side.svg', '된장, 두부, 미역, 가쓰오부시 육수', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('왕새우우동(대)', '큼직한 왕새우가 올라간 우동', 8000, 'MEAL', '/images/menu/side.svg', '우동면, 왕새우, 가쓰오부시 육수, 대파', '밀, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('우동(소)', '따뜻한 기본 우동', 4000, 'MEAL', '/images/menu/side.svg', '우동면, 가쓰오부시 육수, 대파', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('우동(중)', '따뜻한 기본 우동', 5000, 'MEAL', '/images/menu/side.svg', '우동면, 가쓰오부시 육수, 대파', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 음료/주류 (DRINK_ALCOHOL)
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('콜라', '시원한 탄산음료', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '탄산수, 액상과당, 카라멜색소, 카페인', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('녹차', '따뜻한 우롱차', 1500, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '우롱차 잎, 정제수', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 디저트/기타 (DESSERT_ETC) — 뒷주방 케이크/푸딩/아이스크림 보드
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('딸기케이크', '생딸기를 올린 케이크', 3500, 'DESSERT_ETC', '/images/menu/dessert.svg', '빵, 생크림, 딸기, 우유', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('초코케이크', '진한 초콜릿 케이크', 3500, 'DESSERT_ETC', '/images/menu/dessert.svg', '빵, 초콜릿, 생크림, 우유', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('티라미수케이크', '커피향이 진한 이탈리안 디저트', 4000, 'DESSERT_ETC', '/images/menu/dessert.svg', '마스카포네, 커피, 코코아파우더, 빵', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('마카롱', '바삭하고 쫀득한 한입 디저트', 2000, 'DESSERT_ETC', '/images/menu/dessert.svg', '아몬드가루, 설탕, 난백, 버터크림', '난류, 우유, 견과류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('바닐라아이스크림', '부드러운 바닐라 아이스크림', 2500, 'DESSERT_ETC', '/images/menu/dessert.svg', '우유, 생크림, 바닐라빈, 설탕', '우유', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('초코아이스크림', '진한 초콜릿 아이스크림', 2500, 'DESSERT_ETC', '/images/menu/dessert.svg', '우유, 생크림, 코코아파우더, 설탕', '우유', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 포장 (TAKEOUT) — 회전초밥집 포장 메뉴판을 참고한 샘플 데이터. 세트는 묶음 구성을 description에 표기.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('모둠초밥(10P)', '활어2P+연어양파1P+연어2P+초새우1P+소고기직화1P+계란새우1P+구운새우1P+간장새우1P', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 계란, 초밥용 밥', '새우, 갑각류, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('모둠스페셜초밥(12P)', '활어3P+연어양파1P+연어2P+육회타다끼1P+소고기직화1P+육회초밥1P+구운새우1P+간장새우1P+계란새우1P', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 육회, 계란, 초밥용 밥', '새우, 갑각류, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('모둠패밀리초밥세트(29P+우동(대)+새우튀김)', '활어6P+연어4P+연어양파2P+육회타다끼2P+소고기직화2P+육회초밥2P+구운새우2P+간장새우2P+계란새우2P+유부초밥5P+우동(대)+새우튀김', 39900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 육회, 계란, 유부, 우동면, 초밥용 밥', '새우, 갑각류, 난류, 밀, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('연어초밥(10p)포장', '포장 전용 연어초밥 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('연어양파초밥(10p)포장', '포장 전용 연어양파초밥 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 양파, 초밥용 밥, 마요네즈', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('광어초밥(10p)포장', '포장 전용 광어초밥 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '광어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('광어묵은지초밥(10p)포장', '광어와 묵은지 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '광어, 묵은지, 초밥용 밥', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('소고기직화초밥(10p)포장', '직화로 구운 소고기 10피스', 13900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 양념(간장, 설탕, 마늘), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('연어직화초밥(10p)포장', '직화로 구운 연어 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 초밥용 밥, 간장 소스', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('생새우마늘직화초밥(10p)포장', '마늘과 함께 직화로 구운 생새우 10피스', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '생새우, 마늘, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회타다끼초밥(10p)포장', '살짝 겉만 익힌 소고기 타다끼 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 간장 소스', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('구운새우초밥(10p)포장', '구운 새우 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('계란새우초밥(10p)포장', '계란과 새우를 올린 초밥 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '계란, 새우, 초밥용 밥', '난류, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회초밥(5p)+육회타다끼(5p)포장', '육회초밥과 육회타다끼 모듬 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 참기름, 마늘', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('양념새우초밥(10p)포장', '특제 양념을 올린 새우 10피스', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 특제양념, 초밥용 밥', '새우, 갑각류, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회초밥(10p)포장', '포장 전용 육회초밥 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 참기름, 마늘', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('간장새우초밥(10p)포장', '간장 소스를 올린 새우 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 간장 소스, 초밥용 밥', '새우, 갑각류, 대두, 밀', NULL, 0, 0, true,
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
    ('staff_salmon', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '연어다이'), NOW(), NOW()),
    ('staff_hwaleo', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '활어다이'), NOW(), NOW()),
    ('staff_back', '$2a$10$pzbYPSGnLNiueSxEp5THWu7OoQn90uvOWab71Bx/4mLWbzClDpY.u', 'STAFF',
     (SELECT id FROM station WHERE name = '뒷주방'), NOW(), NOW());

-- ============================================
-- 4. 손님 좌석(RestaurantTable) 시드
--    COUNTER(다찌석, 1인용) 1~20번 / TABLE(테이블, 4인용) 1~3번
--    table_number는 seat_type별로 따로 매겨진다 (seat_type+table_number 복합 unique)
-- ============================================
INSERT INTO restaurant_table (seat_type, table_number, seat_count, status, created_at, updated_at) VALUES
    ('COUNTER', 1, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 2, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 3, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 4, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 5, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 6, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 7, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 8, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 9, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 10, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 11, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 12, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 13, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 14, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 15, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 16, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 17, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 18, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 19, 1, 'EMPTY', NOW(), NOW()),
    ('COUNTER', 20, 1, 'EMPTY', NOW(), NOW()),
    ('TABLE', 1, 4, 'EMPTY', NOW(), NOW()),
    ('TABLE', 2, 4, 'EMPTY', NOW(), NOW()),
    ('TABLE', 3, 4, 'EMPTY', NOW(), NOW());

-- ============================================
-- 5. 공지(Notice) 시드
-- ============================================
INSERT INTO notice (title, content, pinned, is_active, created_at, updated_at) VALUES
    ('영업시간 안내', '영업시간은 매일 11:30~22:00이며, 라스트오더는 21:20입니다. 매달 넷째 주 수요일은 정기휴무입니다.', true, true, NOW(), NOW()),
    ('프리미엄 메뉴 한정 수량 안내', '오도로, 이쿠라, 우니 등 프리미엄 초밥은 일일 한정 수량으로 제공되며, 소진 시 품절될 수 있습니다.', false, true, NOW(), NOW()),
    ('오픈 이벤트 종료 안내', '신규 오픈 기념 할인 이벤트는 종료되었습니다. 이용해 주셔서 감사합니다.', false, false, NOW(), NOW());