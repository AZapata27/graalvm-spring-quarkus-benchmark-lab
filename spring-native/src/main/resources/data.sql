
DELETE FROM cart_items;
DELETE FROM products;
DELETE FROM carts;

-- Insertar datos en la tabla "products"
INSERT INTO products (id, name, price, description) VALUES
                                                        (1, 'Product 1', 10.99, 'Description of Product 1'),
                                                        (2, 'Product 2', 15.49, 'Description of Product 2'),
                                                        (3, 'Product 3', 7.25, 'Description of Product 3'),
                                                        (4, 'Product 4', 22.30, 'Description of Product 4'),
                                                        (5, 'Product 5', 5.99, 'Description of Product 5');

-- Insertar datos en la tabla "carts"
INSERT INTO carts (id) VALUES
                                    (1),
                                    (2),
                                    (3);

-- Insertar datos en la tabla "cart_items"
INSERT INTO cart_items (id, product_id, cart_id, quantity) VALUES
                                                               (1, 1, 1, 2),
                                                               (2, 2, 1, 1),
                                                               (3, 3, 2, 3),
                                                               (4, 4, 2, 1),
                                                               (5, 5, 3, 5);
