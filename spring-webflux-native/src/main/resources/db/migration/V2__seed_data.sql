-- Datos de ejemplo. Los ids los genera SERIAL (no se insertan explícitos) para que la
-- secuencia quede sincronizada y los INSERT posteriores de la aplicación no colisionen.
-- Sobre una base recién migrada los ids resultantes son 1..5 (products), 1..3 (carts), 1..5 (cart_items).

-- Insertar datos en la tabla "products"
INSERT INTO products (name, price, description) VALUES
    ('Product 1', 10.99, 'Description of Product 1'),
    ('Product 2', 15.49, 'Description of Product 2'),
    ('Product 3', 7.25, 'Description of Product 3'),
    ('Product 4', 22.30, 'Description of Product 4'),
    ('Product 5', 5.99, 'Description of Product 5');

-- Insertar datos en la tabla "carts"
INSERT INTO carts (user_id) VALUES (1), (2), (3);

-- Insertar datos en la tabla "cart_items"
INSERT INTO cart_items (product_id, cart_id, quantity) VALUES
    (1, 1, 2),
    (2, 1, 1),
    (3, 2, 3),
    (4, 2, 1),
    (5, 3, 5);
