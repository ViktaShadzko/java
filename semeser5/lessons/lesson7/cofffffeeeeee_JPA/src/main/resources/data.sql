-- Insert sample beverages
INSERT INTO BEVERAGE (name, price, description) VALUES ('Espresso', 2.50, 'Strong black coffee');
INSERT INTO BEVERAGE (name, price, description) VALUES ('Cappuccino', 3.50, 'Espresso with steamed milk foam');
INSERT INTO BEVERAGE (name, price, description) VALUES ('Latte', 4.00, 'Espresso with steamed milk');
INSERT INTO BEVERAGE (name, price, description) VALUES ('Americano', 2.75, 'Espresso with hot water');
INSERT INTO BEVERAGE (name, price, description) VALUES ('Mocha', 4.50, 'Espresso with chocolate and steamed milk');

-- Insert sample reviews
INSERT INTO REVIEW (author, rating, comment, beverage_id) VALUES ('John Doe', 5, 'Best espresso in town!', 1);
INSERT INTO REVIEW (author, rating, comment, beverage_id) VALUES ('Jane Smith', 4, 'Good but a bit strong', 1);
INSERT INTO REVIEW (author, rating, comment, beverage_id) VALUES ('Bob Johnson', 5, 'Perfect cappuccino!', 2);
INSERT INTO REVIEW (author, rating, comment, beverage_id) VALUES ('Alice Brown', 3, 'Decent latte, nothing special', 3);

