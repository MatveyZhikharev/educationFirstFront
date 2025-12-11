-- 1. Новые поля
ALTER TABLE users
    ADD COLUMN vk_id BIGINT UNIQUE;

ALTER TABLE users
    ADD COLUMN email VARCHAR(255) NOT NULL;

-- 2. Снимаем default со старых enum-колонок
ALTER TABLE users ALTER COLUMN role DROP DEFAULT;
ALTER TABLE users ALTER COLUMN status DROP DEFAULT;

-- 3. Конвертируем ENUM → VARCHAR
ALTER TABLE users
ALTER COLUMN role TYPE VARCHAR(50)
        USING role::text;

ALTER TABLE users
ALTER COLUMN status TYPE VARCHAR(50)
        USING status::text;

-- 4. Теперь типы больше не используются — можно удалять
DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS user_status;

--VK AUTH STATUS
CREATE TABLE vk_auth_states (
                                state VARCHAR(128) PRIMARY KEY,
                                code_verifier VARCHAR(256) NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
