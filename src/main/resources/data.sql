-- Pre-populate the tb_profile table with default profiles
INSERT INTO profile (id, type, date_created, last_updated)
VALUES (nextval('primary_sequence'), 'CLIENT', NOW(), NOW())
ON CONFLICT (type) DO NOTHING;

INSERT INTO profile (id, type, date_created, last_updated)
VALUES (nextval('primary_sequence'), 'OWNER', NOW(), NOW())
ON CONFLICT (type) DO NOTHING;

-- Mock User 1
INSERT INTO users (id, name, email, auth_id, auth_status, active, date_created, last_updated)
VALUES ('a1b2c3d4-e5f6-7890-1234-567890abcdef', 'John Doe', 'john.doe@example.com', 'auth123', 'CONFIRMED', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Mock Address 1
INSERT INTO address (id, address_line, city, date_created, last_updated, main, neighborhood, number, postal_code, state_province, street_name)
VALUES (1, 'Apt 4B', 'Anytown', NOW(), NOW(), true, 'Downtown', 123, '12345678', 'CA', 'Main Street')
ON CONFLICT (id) DO NOTHING;

-- Link User 1 to Address 1
INSERT INTO user_address (user_id, address_id)
VALUES ('a1b2c3d4-e5f6-7890-1234-567890abcdef', 1)
ON CONFLICT (user_id, address_id) DO NOTHING;

-- Associate User 1 with Profiles
INSERT INTO user_profile (user_id, profile_id)
VALUES ('a1b2c3d4-e5f6-7890-1234-567890abcdef', (SELECT id FROM profile WHERE type = 'CLIENT'))
ON CONFLICT (user_id, profile_id) DO NOTHING;
INSERT INTO user_profile (user_id, profile_id)
VALUES ('a1b2c3d4-e5f6-7890-1234-567890abcdef', (SELECT id FROM profile WHERE type = 'OWNER'))
ON CONFLICT (user_id, profile_id) DO NOTHING;
-- Mock Restaurant
INSERT INTO restaurant (id, name, cnpj, culinary, zone_id, opening_time, closing_time, open_24_hours, active, user_id, date_created, last_updated)
VALUES ('b2c3d4e5-f6a7-8901-2345-67890abcdef1', 'The Good Food Place', '12345678000190', 'Italian', 'America/Sao_Paulo', '11:00:00', '23:00:00', false, true, 'a1b2c3d4-e5f6-7890-1234-567890abcdef', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Link Restaurant to Address 1
INSERT INTO restaurant_address (restaurant_id, address_id)
VALUES ('b2c3d4e5-f6a7-8901-2345-67890abcdef1', 1)
ON CONFLICT (restaurant_id) DO NOTHING;

-- Mock Food Items
INSERT INTO food_item (id, name, description, price, photo_key, delivery_available, available, active, restaurant_id, date_created, last_updated)
VALUES ('c3d4e5f6-a7b8-9012-3456-7890abcdef12', 'Spaghetti Carbonara', 'The classic Italian pasta dish.', 15.99, 'carbonara.jpg', true, true, true, 'b2c3d4e5-f6a7-8901-2345-67890abcdef1', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
INSERT INTO food_item (id, name, description, price, photo_key, delivery_available, available, active, restaurant_id, date_created, last_updated)
VALUES ('d4e5f6a7-b8c9-0123-4567-890abcdef123', 'Margherita Pizza', 'Simple and delicious.', 12.50, 'pizza.jpg', true, true, true, 'b2c3d4e5-f6a7-8901-2345-67890abcdef1', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
-- Mock User 2
INSERT INTO users (id, name, email, auth_id, auth_status, active, date_created, last_updated)
VALUES ('e5f6a7b8-c9d0-1234-5678-90abcdef1234', 'Jane Smith', 'jane.smith@example.com', 'auth456', 'CONFIRMED', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;
-- Mock Address 2 (for User 2)
INSERT INTO address (id, address_line, city, date_created, last_updated, main, neighborhood, number, postal_code, state_province, street_name)
VALUES (nextval('primary_sequence'), 'House', 'Otherplace', NOW(), NOW(), true, 'Suburbia', 456, '54321876', 'NY', 'Oak Avenue')
ON CONFLICT (id) DO NOTHING;
-- Link User 2 to Address 2
INSERT INTO user_address (user_id, address_id)
VALUES ('e5f6a7b8-c9d0-1234-5678-90abcdef1234', currval('primary_sequence'))
ON CONFLICT (user_id, address_id) DO NOTHING;
-- Associate User 2 with Profile
INSERT INTO user_profile (user_id, profile_id)
VALUES ('e5f6a7b8-c9d0-1234-5678-90abcdef1234', (SELECT id FROM profile WHERE type = 'CLIENT'))
ON CONFLICT (user_id, profile_id) DO NOTHING;
