-- ============================================================
--   DIGITAL AUCTION SYSTEM - SAMPLE DATA
--   Run this file in Oracle APEX > SQL Commands
--   (Run each block separately if needed)
-- ============================================================

-- ============================================================
-- STEP 1: CATEGORIES
-- ============================================================
INSERT INTO CATEGORY (category_id, name, description)
VALUES (category_seq.NEXTVAL, 'Electronics', 'Smartphones, laptops, gadgets and tech accessories');

INSERT INTO CATEGORY (category_id, name, description)
VALUES (category_seq.NEXTVAL, 'Collectibles & Art', 'Rare collectibles, paintings, antiques and memorabilia');

INSERT INTO CATEGORY (category_id, name, description)
VALUES (category_seq.NEXTVAL, 'Fashion & Luxury', 'Designer clothing, watches, handbags and luxury goods');

COMMIT;

-- ============================================================
-- STEP 2: USERS (1 Admin + 5 Sellers + 10 Buyers)
-- ============================================================

-- ADMIN
INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Rajesh Khanna', 'admin@bidzone.com', 'admin123', '9800000001', '12 Admin Tower, New Delhi', 'ADMIN');

-- SELLERS
INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Amit Verma', 'amit.verma@seller.com', 'seller123', '9811111111', '45 Connaught Place, New Delhi', 'SELLER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Priya Nair', 'priya.nair@seller.com', 'seller123', '9822222222', '88 Marine Drive, Mumbai', 'SELLER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Suresh Reddy', 'suresh.reddy@seller.com', 'seller123', '9833333333', '23 Banjara Hills, Hyderabad', 'SELLER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Meena Pillai', 'meena.pillai@seller.com', 'seller123', '9844444444', '67 Brigade Road, Bengaluru', 'SELLER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Rohan Desai', 'rohan.desai@seller.com', 'seller123', '9855555555', '14 SB Road, Pune', 'SELLER');

-- BUYERS
INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Ananya Singh', 'ananya.singh@buyer.com', 'buyer123', '9866666661', '7 Park Street, Kolkata', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Vikram Malhotra', 'vikram.m@buyer.com', 'buyer123', '9866666662', '33 Sector 17, Chandigarh', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Deepika Joshi', 'deepika.j@buyer.com', 'buyer123', '9866666663', '5 Civil Lines, Jaipur', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Karan Mehta', 'karan.mehta@buyer.com', 'buyer123', '9866666664', '19 Worli Sea Face, Mumbai', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Neha Gupta', 'neha.gupta@buyer.com', 'buyer123', '9866666665', '2 Karol Bagh, New Delhi', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Arjun Sharma', 'arjun.sharma@buyer.com', 'buyer123', '9866666666', '55 MG Road, Bengaluru', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Pooja Iyer', 'pooja.iyer@buyer.com', 'buyer123', '9866666667', '10 Anna Nagar, Chennai', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Rahul Bose', 'rahul.bose@buyer.com', 'buyer123', '9866666668', '8 Hazratganj, Lucknow', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Simran Kaur', 'simran.kaur@buyer.com', 'buyer123', '9866666669', '3 Hall Bazaar, Amritsar', 'BUYER');

INSERT INTO USERS (user_id, name, email, password, phone, address, role)
VALUES (users_seq.NEXTVAL, 'Tarun Sharma', 'tarun@auction.com', 'auction123', '9866666670', 'Indore, Madhya Pradesh', 'BUYER');

COMMIT;

-- ============================================================
-- STEP 3: AUCTION EVENTS (conducted by Admin = user_id 1)
-- ============================================================
INSERT INTO AUCTION_EVENT (auction_id, title, description, start_time, end_time, status, conducted_by)
VALUES (auction_seq.NEXTVAL,
        'Spring Tech Extravaganza 2026',
        'Bid on the latest electronics — laptops, phones, and more!',
        TIMESTAMP '2026-04-10 10:00:00',
        TIMESTAMP '2026-04-20 22:00:00',
        'ONGOING', 1);

INSERT INTO AUCTION_EVENT (auction_id, title, description, start_time, end_time, status, conducted_by)
VALUES (auction_seq.NEXTVAL,
        'Luxury & Collectibles Night',
        'Rare collectibles, luxury watches and designer fashion up for auction.',
        TIMESTAMP '2026-04-12 18:00:00',
        TIMESTAMP '2026-04-22 20:00:00',
        'ONGOING', 1);

COMMIT;

-- ============================================================
-- STEP 4: ITEMS (10 items linked to sellers & auctions)
-- ============================================================

-- Electronics Auction (auction_id=1, category_id=1)
INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Apple MacBook Pro M3',
        '14-inch MacBook Pro with M3 chip, 16GB RAM, 512GB SSD. Barely used, excellent condition.',
        85000.00, 'macbook.jpg', 'AVAILABLE', 2, 1, 1);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Samsung Galaxy S24 Ultra',
        '256GB, Titanium Black. Sealed box with all accessories. 12-month warranty.',
        72000.00, 'galaxy_s24.jpg', 'AVAILABLE', 3, 1, 1);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Sony WH-1000XM5 Headphones',
        'Industry-leading noise cancelling wireless headphones. Midnight Black.',
        18000.00, 'sony_headphones.jpg', 'AVAILABLE', 4, 1, 1);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'DJI Mini 4 Pro Drone',
        '4K/60fps drone with 3-axis gimbal and 34-min flight time. Like new.',
        45000.00, 'dji_drone.jpg', 'AVAILABLE', 5, 1, 1);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Dell XPS 15 Laptop',
        'Intel Core i9, 32GB RAM, 1TB NVMe SSD, OLED 3.5K display. 2024 model.',
        95000.00, 'dell_xps.jpg', 'AVAILABLE', 6, 1, 1);

-- Luxury & Collectibles Auction (auction_id=2)
INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Rolex Submariner Watch',
        'Ref. 124060, 41mm, Oystersteel black dial. Box and papers included. 2023.',
        750000.00, 'rolex_sub.jpg', 'AVAILABLE', 2, 2, 3);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Louis Vuitton Neverfull MM',
        'Authentic LV Neverfull MM in Damier Ebene canvas. Excellent condition with dustbag.',
        58000.00, 'lv_bag.jpg', 'AVAILABLE', 3, 2, 3);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Vintage Indian Miniature Painting',
        '18th century Mughal-era miniature painting on ivory. Certified authentic by Sotheby''s.',
        200000.00, 'miniature_painting.jpg', 'AVAILABLE', 4, 2, 2);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Signed Sachin Tendulkar Bat',
        'Full-size MRF bat personally signed by Sachin Tendulkar. With certificate of authenticity.',
        35000.00, 'sachin_bat.jpg', 'AVAILABLE', 5, 2, 2);

INSERT INTO ITEM (item_id, name, description, base_price, image_url, status, seller_id, auction_id, category_id)
VALUES (item_seq.NEXTVAL, 'Swarovski Crystal Figurine Set',
        'Limited edition Swarovski Annual Edition 2024 figurine set. Never displayed.',
        22000.00, 'swarovski.jpg', 'AVAILABLE', 6, 2, 2);

COMMIT;

-- ============================================================
-- STEP 5: SAMPLE BIDS (to show bidding activity)
-- ============================================================
-- Bids on MacBook Pro (item_id=1)
INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 87000.00, TIMESTAMP '2026-04-11 10:15:00', 1, 7);

INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 89500.00, TIMESTAMP '2026-04-12 14:30:00', 1, 8);

INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 92000.00, TIMESTAMP '2026-04-13 09:00:00', 1, 9);

-- Bids on Samsung Galaxy (item_id=2)
INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 73500.00, TIMESTAMP '2026-04-11 11:00:00', 2, 10);

INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 75000.00, TIMESTAMP '2026-04-12 16:45:00', 2, 11);

-- Bids on Rolex Watch (item_id=6)
INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 760000.00, TIMESTAMP '2026-04-13 18:00:00', 6, 12);

INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 780000.00, TIMESTAMP '2026-04-13 19:30:00', 6, 13);

INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 800000.00, TIMESTAMP '2026-04-14 00:00:00', 6, 14);

-- Bids on Sachin Bat (item_id=9)
INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 36000.00, TIMESTAMP '2026-04-13 12:00:00', 9, 15);

INSERT INTO BID (bid_id, bid_amount, bid_time, item_id, buyer_id)
VALUES (bid_seq.NEXTVAL, 38500.00, TIMESTAMP '2026-04-13 15:20:00', 9, 16);

COMMIT;

-- ============================================================
-- VERIFICATION QUERIES (Run after inserting to verify)
-- ============================================================
-- SELECT COUNT(*) AS total_users   FROM USERS;
-- SELECT COUNT(*) AS total_items   FROM ITEM;
-- SELECT COUNT(*) AS total_bids    FROM BID;
-- SELECT COUNT(*) AS total_auctions FROM AUCTION_EVENT;

-- ============================================================
-- LOGIN CREDENTIALS SUMMARY
-- ============================================================
-- ADMIN   : admin@bidzone.com     / admin123
-- SELLER  : amit.verma@seller.com / seller123
-- BUYER   : tarun@auction.com     / auction123
-- ============================================================
