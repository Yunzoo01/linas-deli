INSERT INTO platter(platter_id, platter_name) VALUES
                                                (1,'set A'),
                                                (2,'set B'),
                                                (3,'set C');

INSERT INTO platter_order (platter_id, customer_name, phone, email, date, time, status, created_at, updated_at) VALUES
                                                                                                            (1, 'John Doe', '123-456-7890', 'john@example.com', '2024-02-20', 12, 'completed', '2024-02-20 10:00:00', '2024-02-20 10:00:00'),
                                                                                                            (1, 'Jane Doe', '987-654-3210', 'jane@example.com', '2024-02-21', 14, 'completed', '2024-02-21 11:30:00', '2024-02-21 11:30:00'),
                                                                                                            (2, 'Alice Smith', '111-222-3333', 'alice@example.com', '2024-02-22', 16, 'completed', '2024-02-22 14:45:00', '2024-02-22 14:45:00'),
                                                                                                            (3, 'Bob Brown', '444-555-6666', 'bob@example.com', '2024-02-23', 18, 'in progress', '2024-02-23 16:15:00', '2024-02-23 16:15:00'),
                                                                                                            (2, 'Charlie White', '777-888-9999', 'charlie@example.com', '2024-02-24', 20, 'decline', '2024-02-24 18:00:00', '2024-02-24 18:00:00');
-- 첫 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-03-01 00:00:00', '2025-03-10 23:59:59', 'Promotion from 2025-03-01 to 2025-03-10', 'promo_1_image.png', '/upload/promo_1_image.png', now(), now());

-- 두 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-03-11 00:00:00', '2025-03-20 23:59:59', 'Promotion from 2025-03-11 to 2025-03-20', 'promo_2_image.png', '/upload/promo_2_image.png', now(), now());

-- 세 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-03-21 00:00:00', '2025-03-30 23:59:59', 'Promotion from 2025-03-21 to 2025-03-30', 'promo_3_image.png', '/upload/promo_3_image.png', now(), now());

-- 네 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-04-01 00:00:00', '2025-04-10 23:59:59', 'Promotion from 2025-04-01 to 2025-04-10', 'promo_4_image.png', '/upload/promo_4_image.png', now(), now());

-- 다섯 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-04-11 00:00:00', '2025-04-20 23:59:59', 'Promotion from 2025-04-11 to 2025-04-20', 'promo_5_image.png', '/upload/promo_5_image.png', now(), now());

-- 여섯 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-04-21 00:00:00', '2025-04-30 23:59:59', 'Promotion from 2025-04-21 to 2025-04-30', 'promo_6_image.png', '/upload/promo_6_image.png', now(), now());

-- 일곱 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-05-01 00:00:00', '2025-05-10 23:59:59', 'Promotion from 2025-05-01 to 2025-05-10', 'promo_7_image.png', '/upload/promo_7_image.png', now(), now());

-- 여덟 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-05-11 00:00:00', '2025-05-20 23:59:59', 'Promotion from 2025-05-11 to 2025-05-20', 'promo_8_image.png', '/upload/promo_8_image.png', now(), now());

-- 아홉 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-05-21 00:00:00', '2025-05-30 23:59:59', 'Promotion from 2025-05-21 to 2025-05-30', 'promo_9_image.png', '/upload/promo_9_image.png', now(), now());

-- 열 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-06-01 00:00:00', '2025-06-10 23:59:59', 'Promotion from 2025-06-01 to 2025-06-10', 'promo_10_image.png', '/upload/promo_10_image.png', now(), now());

-- 열한 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-06-11 00:00:00', '2025-06-20 23:59:59', 'Promotion from 2025-06-11 to 2025-06-20', 'promo_11_image.png', '/upload/promo_11_image.png', now(), now());

-- 열두 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-06-21 00:00:00', '2025-06-30 23:59:59', 'Promotion from 2025-06-21 to 2025-06-30', 'promo_12_image.png', '/upload/promo_12_image.png', now(), now());

-- 열세 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-07-01 00:00:00', '2025-07-10 23:59:59', 'Promotion from 2025-07-01 to 2025-07-10', 'promo_13_image.png', '/upload/promo_13_image.png', now(), now());

-- 열네 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-07-11 00:00:00', '2025-07-20 23:59:59', 'Promotion from 2025-07-11 to 2025-07-20', 'promo_14_image.png', '/upload/promo_14_image.png', now(), now());

-- 열다섯 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-07-21 00:00:00', '2025-07-31 23:59:59', 'Promotion from 2025-07-21 to 2025-07-31', 'promo_15_image.png', '/upload/promo_15_image.png', now(), now());

-- 열여섯 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-08-01 00:00:00', '2025-08-10 23:59:59', 'Promotion from 2025-08-01 to 2025-08-10', 'promo_16_image.png', '/upload/promo_16_image.png', now(), now());

-- 열일곱 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-08-11 00:00:00', '2025-08-20 23:59:59', 'Promotion from 2025-08-11 to 2025-08-20', 'promo_17_image.png', '/upload/promo_17_image.png', now(), now());

-- 열여덟 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-08-21 00:00:00', '2025-08-31 23:59:59', 'Promotion from 2025-08-21 to 2025-08-31', 'promo_18_image.png', '/upload/promo_18_image.png', now(), now());

-- 열아홉 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-09-01 00:00:00', '2025-09-10 23:59:59', 'Promotion from 2025-09-01 to 2025-09-10', 'promo_19_image.png', '/upload/promo_19_image.png', now(), now());

-- 스무 번째 프로모션
INSERT INTO promotion (start_date, end_date, promotion_title, promotion_image_name, promotion_image_url, created_at, updated_at)
VALUES
    ('2025-09-11 00:00:00', '2025-09-20 23:59:59', 'Promotion from 2025-09-11 to 2025-09-20', 'promo_20_image.png', '/upload/promo_20_image.png', now(), now());

