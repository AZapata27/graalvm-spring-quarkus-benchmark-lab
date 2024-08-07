-- Crear tabla "products" si no existe
CREATE TABLE IF NOT EXISTS products (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        price DECIMAL(10, 2) NOT NULL,
                                        description TEXT
);

-- Crear tabla "carts" si no existe
CREATE TABLE IF NOT EXISTS carts (
                                     id SERIAL PRIMARY KEY,
                                     user_id BIGINT NOT NULL
);

-- Crear tabla "cart_items" si no existe
CREATE TABLE IF NOT EXISTS cart_items (
                                          id SERIAL PRIMARY KEY,
                                          product_id INT NOT NULL,
                                          cart_id INT NOT NULL,
                                          quantity INT NOT NULL,
                                          FOREIGN KEY (product_id) REFERENCES products(id),
                                          FOREIGN KEY (cart_id) REFERENCES carts(id)
);
