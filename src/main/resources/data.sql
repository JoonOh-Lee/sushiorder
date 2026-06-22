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
-- ============================================

-- 초밥 (SUSHI)
INSERT INTO menu (name, description, price, category, image_url, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
                                                                                                                                                                     ('연어초밥', '신선한 노르웨이산 연어', 3500, 'SUSHI', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '연어다이'), 0, NOW(), NOW()),
                                                                                                                                                                     ('참치초밥', '두툼한 참치 뱃살', 4500, 'SUSHI', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
                                                                                                                                                                     ('광어초밥', '쫄깃한 식감의 광어', 4000, 'SUSHI', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW()),
                                                                                                                                                                     ('새우초밥', '탱글탱글한 단새우', 3000, 'SUSHI', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
                                                                                                                                                                     ('우니초밥', '오늘의 한정 성게알 — 수량 한정', 8000, 'SUSHI', NULL, 5, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '활어다이'), 0, NOW(), NOW());

-- 롤 (ROLL)
INSERT INTO menu (name, description, price, category, image_url, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
                                                                                                                                                                     ('캘리포니아롤', '게살과 아보카도', 6000, 'ROLL', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW()),
                                                                                                                                                                     ('스파이시참치롤', '매콤한 참치 마요', 6500, 'ROLL', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '아부리다이'), 0, NOW(), NOW());

-- 사이드 (SIDE)
INSERT INTO menu (name, description, price, category, image_url, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
                                                                                                                                                                     ('미소시루', '따뜻한 일본 된장국', 2000, 'SIDE', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
                                                                                                                                                                     ('가라아게', '바삭한 일본식 닭튀김', 5000, 'SIDE', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 음료 (DRINK)
INSERT INTO menu (name, description, price, category, image_url, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
                                                                                                                                                                     ('콜라', '시원한 탄산음료', 2000, 'DRINK', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW()),
                                                                                                                                                                     ('녹차', '따뜻한 우롱차', 1500, 'DRINK', NULL, NULL, 0, 0, true,
                                                                                                                                                                      (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- 디저트 (DESSERT)
INSERT INTO menu (name, description, price, category, image_url, stock_count, like_count, dislike_count, is_active, station_id, version, created_at, updated_at) VALUES
    ('모찌 아이스크림', '오늘의 한정 디저트', 3000, 'DESSERT', NULL, 10, 0, 0, true,
     (SELECT id FROM station WHERE name = '뒷주방'), 0, NOW(), NOW());

-- ============================================
-- 3. 직원(Staff) 시드 — 로컬 개발용 admin 계정 (admin / admin1234)
-- ============================================
INSERT INTO staff (username, password, role, created_at, updated_at) VALUES
    ('admin', '$2a$10$dkfi6Vcq77YzbrGX42Cvd.LVr4OzdHl0ijKnFIsg0OYcFkH79mZGG', 'ADMIN', NOW(), NOW());