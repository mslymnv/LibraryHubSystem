
CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000),
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    published_year INTEGER,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_books_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

