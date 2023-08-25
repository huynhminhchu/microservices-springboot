CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    product_id INTEGER,
    review_id INTEGER,
    author VARCHAR(255),
    content VARCHAR(255),
    subject VARCHAR(255)
);