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
    ('엔가와초밥', '광어 살 중에서도 제일 쫄깃한 부위만 골랐어요', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '광어 지느러미살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('연어뱃살초밥', '기름이 좔좔, 한 입이면 끝나는 맛', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '연어 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('도미뱃살초밥', '담백함과 고소함을 동시에 잡았어요', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '도미 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('오도로초밥', '참치 중 가장 귀한 부위, 오늘만 한정 수량', 1990, 'PREMIUM_SUSHI', NULL, '참다랑어 뱃살, 초밥용 밥, 와사비', '없음', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('메가도로초밥', '고소함 가득, 황새치 뱃살 한 점', 1990, 'PREMIUM_SUSHI', NULL, '황새치 뱃살, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('이쿠라군함', '톡톡 터지는 연어알, 오늘만 만날 수 있어요', 1990, 'PREMIUM_SUSHI', NULL, '연어알, 초밥용 밥, 김', '난류', 8, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('안키모초밥', '바다의 푸아그라라 불리는 그 맛, 아귀간', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '아귀 간, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('우니군함', '크리미한 우니, 한정 수량이라 서둘러야 해요', 1990, 'PREMIUM_SUSHI', NULL, '성게알, 초밥용 밥, 김', '없음', 5, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('블랙타이거새우초밥', '한 점만으로 입이 꽉 차는 블랙타이거새우', 1990, 'PREMIUM_SUSHI', '/images/menu/sushi.svg', '블랙타이거새우, 초밥용 밥, 와사비', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW());

-- 신선초밥 (FRESH_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('광어초밥', '쫄깃함이 살아있는 활광어 초밥', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '광어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('연어초밥', '노르웨이에서 갓 온 신선한 연어', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '연어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('연어양파초밥', '연어 위에 양파 톡톡, 알싱한 조합', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '연어, 양파, 초밥용 밥, 마요네즈', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('참돔마스카와초밥', '껍질까지 맛있는 참돔, 마스카와로 즐겨요', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '참돔, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('활어묵은지초밥', '잘 익은 묵은지와 활어의 의외로 찰떡인 만남', 1990, 'FRESH_SUSHI', '/images/menu/sushi.svg', '활어, 묵은지, 초밥용 밥', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW());

-- 참치초밥 (TUNA_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('참치초밥', '깔끔하고 담백한 참치 한 점', 1990, 'TUNA_SUSHI', '/images/menu/sushi.svg', '참치, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW());

-- 고기초밥 (MEAT_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('육회초밥', '신선한 소고기 육회, 고소함이 입에 착', 1990, 'MEAT_SUSHI', '/images/menu/sushi.svg', '소고기, 초밥용 밥, 참기름, 마늘', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW());

-- 군함초밥 (GUNKAN_SUSHI) — 1접시 1,990원
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('날치알군함', '톡톡 씹히는 날치알을 가득 올렸어요', 1990, 'GUNKAN_SUSHI', '/images/menu/sushi.svg', '날치알, 초밥용 밥, 김, 마요네즈', '난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW());

-- 구운초밥 (GRILLED_SUSHI) — 직접 구운 스테이크류. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('소불고기스테이크초밥', '달콤짭짤한 불고기 스테이크, 인기 메뉴예요', 3500, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '소고기, 양념(간장, 설탕, 마늘), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('육회타다끼초밥', '겉만 살짝, 속은 촉촉한 소고기 타다끼', 3500, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '소고기, 초밥용 밥, 간장 소스', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('관자스테이크초밥', '버터향 솔솔, 노릇하게 구운 관자', 4000, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '관자, 버터, 초밥용 밥', '조개류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('생새우갈릭스테이크초밥', '마늘버터 향 가득, 통통한 생새우 스테이크', 4000, 'GRILLED_SUSHI', '/images/menu/sushi.svg', '생새우, 마늘, 버터, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 양념초밥 (SEASONED_SUSHI) — 양념을 발라 구운 종류. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('도미양념구이초밥', '특제 양념 발라 노릇하게 구운 도미', 3500, 'SEASONED_SUSHI', '/images/menu/sushi.svg', '도미, 양념(간장, 미림), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 튀김류 (FRIED) — 뒷주방 튀김 보드. 가격은 추정치.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('새우튀김', '겉바속촉, 튀김계의 정석 새우튀김', 3500, 'FRIED', '/images/menu/side.svg', '새우, 튀김가루, 식용유', '새우, 갑각류, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('가라아게', '바삭바삭 일본식 치킨, 가라아게', 3500, 'FRIED', '/images/menu/side.svg', '닭고기, 튀김가루, 식용유', '밀, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 식사류 (MEAL) — 우동/맑은탕 보드. 가격은 보드 표기, 미소시루는 추정가.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('미소시루', '속을 따뜻하게 채워주는 일본식 된장국', 2000, 'MEAL', '/images/menu/side.svg', '된장, 두부, 미역, 가쓰오부시 육수', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('왕새우우동(대)', '왕새우 한가득, 든든하게 즐기는 우동', 8000, 'MEAL', '/images/menu/side.svg', '우동면, 왕새우, 가쓰오부시 육수, 대파', '밀, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('우동(소)', '뜨끈한 국물이 생각날 때 딱, 기본 우동', 4000, 'MEAL', '/images/menu/side.svg', '우동면, 가쓰오부시 육수, 대파', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('우동(중)', '한 그릇 든든하게, 기본 우동', 5000, 'MEAL', '/images/menu/side.svg', '우동면, 가쓰오부시 육수, 대파', '밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 음료/주류 (DRINK_ALCOHOL)
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('콜라', '탄산 가득, 시원하게 한 잔', 2000, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '탄산수, 액상과당, 카라멜색소, 카페인', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('녹차', '느긋하게 즐기는 따뜻한 우롱차', 1500, 'DRINK_ALCOHOL', '/images/menu/drink.svg', '우롱차 잎, 정제수', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 디저트/기타 (DESSERT_ETC) — 뒷주방 케이크/푸딩/아이스크림 보드
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('딸기케이크', '상큼한 생딸기를 듬뿍 올렸어요', 3500, 'DESSERT_ETC', '/images/menu/dessert.svg', '빵, 생크림, 딸기, 우유', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('초코케이크', '진한 초콜릿에 풍덩, 달콤한 마무리', 3500, 'DESSERT_ETC', '/images/menu/dessert.svg', '빵, 초콜릿, 생크림, 우유', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('티라미수케이크', '은은한 커피향, 정통 이탈리안 디저트', 4000, 'DESSERT_ETC', '/images/menu/dessert.svg', '마스카포네, 커피, 코코아파우더, 빵', '우유, 밀, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('마카롱', '바삭 쫀득, 한입에 행복해지는 마카롱', 2000, 'DESSERT_ETC', '/images/menu/dessert.svg', '아몬드가루, 설탕, 난백, 버터크림', '난류, 우유, 견과류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('바닐라아이스크림', '부드럽게 녹는 바닐라 아이스크림', 2500, 'DESSERT_ETC', '/images/menu/dessert.svg', '우유, 생크림, 바닐라빈, 설탕', '우유', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('초코아이스크림', '진하게 즐기는 초코 아이스크림', 2500, 'DESSERT_ETC', '/images/menu/dessert.svg', '우유, 생크림, 코코아파우더, 설탕', '우유', 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 포장 (TAKEOUT) — 회전초밥집 포장 메뉴판을 참고한 샘플 데이터. 세트는 묶음 구성을 description에 표기.
INSERT INTO menu (name, description, price, category, image_url, ingredients, allergy_info, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('모둠초밥(10P)', '혼자 먹어도 든든한 모둠 10피스 — 활어2P+연어양파1P+연어2P+초새우1P+소고기직화1P+계란새우1P+구운새우1P+간장새우1P', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 계란, 초밥용 밥', '새우, 갑각류, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('모둠스페셜초밥(12P)', '한 단계 업그레이드한 스페셜 12피스 — 활어3P+연어양파1P+연어2P+육회타다끼1P+소고기직화1P+육회초밥1P+구운새우1P+간장새우1P+계란새우1P', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 육회, 계란, 초밥용 밥', '새우, 갑각류, 난류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('모둠패밀리초밥세트(29P+우동(대)+새우튀김)', '다 같이 먹기 딱 좋은 패밀리세트 — 활어6P+연어4P+연어양파2P+육회타다끼2P+소고기직화2P+육회초밥2P+구운새우2P+간장새우2P+계란새우2P+유부초밥5P+우동(대)+새우튀김', 39900, 'TAKEOUT', '/images/menu/pojang_menu.png', '활어, 연어, 새우, 소고기, 육회, 계란, 유부, 우동면, 초밥용 밥', '새우, 갑각류, 난류, 밀, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
    ('연어초밥(10p)포장', '포장으로도 신선하게, 연어초밥 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('연어양파초밥(10p)포장', '양파 토핑까지 챙긴 연어양파초밥 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 양파, 초밥용 밥, 마요네즈', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('광어초밥(10p)포장', '포장으로 즐기는 쫄깃한 광어초밥 10피스', 17900, 'TAKEOUT', '/images/menu/pojang_menu.png', '광어, 초밥용 밥, 와사비', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('광어묵은지초밥(10p)포장', '광어와 묵은지의 색다른 조합 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '광어, 묵은지, 초밥용 밥', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
    ('소고기직화초밥(10p)포장', '불맛 가득 직화 소고기 10피스', 13900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 양념(간장, 설탕, 마늘), 초밥용 밥', '대두, 밀', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('연어직화초밥(10p)포장', '불맛 입힌 직화 연어 10피스', 18900, 'TAKEOUT', '/images/menu/pojang_menu.png', '연어, 초밥용 밥, 간장 소스', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
    ('생새우마늘직화초밥(10p)포장', '마늘향 솔솔, 직화 생새우 10피스', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '생새우, 마늘, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회타다끼초밥(10p)포장', '겉만 살짝 익힌 소고기 타다끼 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 간장 소스', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('구운새우초밥(10p)포장', '노릇하게 구운 새우 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 초밥용 밥', '새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('계란새우초밥(10p)포장', '계란과 새우 듀오, 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '계란, 새우, 초밥용 밥', '난류, 새우, 갑각류', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회초밥(5p)+육회타다끼(5p)포장', '육회 두 가지 맛을 한 번에, 모둠 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 참기름, 마늘', '대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
    ('양념새우초밥(10p)포장', '특제 양념이 매력 포인트, 새우 10피스', 14900, 'TAKEOUT', '/images/menu/pojang_menu.png', '새우, 특제양념, 초밥용 밥', '새우, 갑각류, 대두', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
    ('육회초밥(10p)포장', '포장으로도 신선하게, 육회초밥 10피스', 19900, 'TAKEOUT', '/images/menu/pojang_menu.png', '소고기, 초밥용 밥, 참기름, 마늘', '없음', NULL, 0, 0, true,
     (SELECT id FROM station WHERE name = '육회다이'), 0, NOW(), NOW()),
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
    ('영업시간 꼭 확인하세요 ⏰', '매일 11:30~22:00까지 영업해요 (라스트오더 21:20!) 매달 넷째 주 수요일은 쉬어가는 날이니 참고해주세요 🙏', true, true, NOW(), NOW()),
    ('프리미엄 메뉴, 빨리 픽하세요', '오도로·이쿠라·우니 같은 프리미엄 초밥은 매일 한정 수량만 준비돼요. 소진되면 바로 다음 날 만나요!', false, true, NOW(), NOW()),
    ('오픈 이벤트가 마무리됐어요', '오픈 기념 할인 이벤트는 종료됐어요. 그동안 찾아주셔서 정말 감사해요 :)', false, false, NOW(), NOW());
