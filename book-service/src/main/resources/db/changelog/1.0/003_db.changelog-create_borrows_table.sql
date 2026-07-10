
CREATE TABLE IF NOT EXISTS borrows (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    borrowed_at TIMESTAMP NOT NULL,
    due_date DATE NOT NULL,
    returned_at TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    book_id BIGINT,
    CONSTRAINT fk_borrows_books FOREIGN KEY (book_id) REFERENCES books(id)
);
