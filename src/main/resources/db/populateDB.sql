DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM dishes;
DELETE FROM restaurants;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
('User', 'user@world.org', '$2a$10$YEd4fMinfXtgYxvK2gt/XeoXMMMgbKPtMYbdAUPWvIGGjw0BL2BkS'),
('Admin', 'admin@world.org', '$2a$10$kBH8ogsISrZP7h9k4TvTj.dC4mUUtxgWtz1EZASWctj9tyVYnxptG');

INSERT INTO user_roles (role, user_id) VALUES
('USER', 100000),
('ADMIN', 100001),
('USER', 100001);

INSERT INTO restaurants (name) VALUES
('Прага'),
('У Дяди Федора');

INSERT INTO dishes (name, date, price, restaurant_id) VALUES
('Fish', today(), 100, 100002),
('Chips', today(), 50, 100002),
('Still water', today(), 15, 100002),
('Soup', today(), 100, 100003),
('Steak', today(), 250, 100003);
