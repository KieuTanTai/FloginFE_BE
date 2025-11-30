-- Categories
INSERT INTO categories (name, description) VALUES
('Gaming Laptop', 'High-performance laptops for gaming'),
('Business Laptop', 'Professional laptops for work'),
('Ultrabook', 'Thin and portable laptops'),
('Workstation', 'High-end laptops for creative work'),
('Budget Laptop', 'Affordable laptops'),
('2-in-1 Laptop', 'Convertible laptops with touchscreen'),
('Chromebook', 'Cloud-based laptops');

-- Accounts
INSERT INTO accounts (username, password) VALUES
('admin', '$2a$10$Dskyoq6HmReLPO.oQzHW9Ou.ErYJdntI5R88qbcgGidELa77kSXHy'),
('john_doe', '$2a$10$0X1PZGtoQOeS9dz2BWws.OqYtY5mTeldc37Gv7HJRTJdwT1u/t6Ge');

-- Products (link category by id)
INSERT INTO products (name, image_url, price, description, category_id, quantity, deleted) VALUES
('ASUS ROG Strix G15', 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=400', 1299.99, 'Powerful gaming laptop', 1, 15, FALSE),
('Dell XPS 13', 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=400', 1099.99, 'Ultra-portable business laptop', 2, 20, FALSE),
('MacBook Air M2', 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400', 1199.99, 'Lightweight ultrabook', 3, 25, FALSE);
