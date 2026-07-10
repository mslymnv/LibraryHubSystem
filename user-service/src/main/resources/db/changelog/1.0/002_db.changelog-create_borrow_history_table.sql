CREATE TABLE borrow_history
(
    id          BIGSERIAL PRIMARY KEY,
    book_id     BIGINT       NOT NULL,
    book_title  VARCHAR(255) NOT NULL,
    borrowed_at TIMESTAMP    NOT NULL,
    returned_at TIMESTAMP,
    status      VARCHAR(50)  NOT NULL,
    user_id     BIGINT,
    CONSTRAINT fk_borrow_history_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);