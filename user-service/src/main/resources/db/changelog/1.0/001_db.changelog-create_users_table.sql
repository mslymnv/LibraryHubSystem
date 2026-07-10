CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    full_name  VARCHAR(255),
    status     VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE user_roles
(
    user_id BIGINT      NOT NULL,
    role    VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);