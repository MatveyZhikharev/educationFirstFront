-- ==============
-- ENUM TYPES
-- ==============
DO $$ BEGIN
CREATE TYPE user_status AS ENUM ('registered', 'active', 'blocked');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
CREATE TYPE user_role AS ENUM ('user', 'admin');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- ==============
-- EXTENSIONS
-- ==============
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ==============
-- SEQUENCES
-- ==============
CREATE SEQUENCE block_sort_seq START 1;

-- ==============
-- USERS
-- ==============
CREATE TABLE users (
                       id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       first_name          VARCHAR(100) NOT NULL,
                       last_name           VARCHAR(100) NOT NULL,
                       status              user_status NOT NULL DEFAULT 'registered',
                       registration_date   TIMESTAMP NOT NULL DEFAULT NOW(),
                       payment_date        TIMESTAMP,
                       role                user_role NOT NULL DEFAULT 'user'
);

-- ==============
-- TESTS
-- ==============
CREATE TABLE tests (
                       id              BIGSERIAL PRIMARY KEY,
                       pass_percent    INT CHECK (pass_percent >= 0 AND pass_percent <= 100)
);

-- ==============
-- BLOCKS
-- ==============
CREATE TABLE blocks (
                        id              BIGSERIAL PRIMARY KEY,
                        content_id      BIGINT,
                        title           VARCHAR(255) NOT NULL,
                        image_id        BIGINT,
                        sort_order      INT NOT NULL DEFAULT nextval('block_sort_seq'),
                        test_id         BIGINT REFERENCES tests(id) ON DELETE CASCADE,
                        video_id        BIGINT,
                        CONSTRAINT chk_test_or_video CHECK (
                            (test_id IS NOT NULL AND video_id IS NULL)
                                OR (test_id IS NULL AND video_id IS NOT NULL)
                            )
);

-- ==============
-- QUESTIONS
-- ==============
CREATE TABLE questions (
                           id              BIGSERIAL PRIMARY KEY,
                           test_id         BIGINT NOT NULL REFERENCES tests(id) ON DELETE CASCADE,
                           question_text   TEXT NOT NULL
);

-- ==============
-- ANSWERS
-- ==============
CREATE TABLE answers (
                         id              BIGSERIAL PRIMARY KEY,
                         question_id     BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
                         answer_text     TEXT NOT NULL,
                         is_right        BOOLEAN NOT NULL DEFAULT FALSE
);

-- ==============
-- INDEXES
-- ==============
CREATE INDEX idx_questions_test_id ON questions(test_id);
CREATE INDEX idx_answers_question_id ON answers(question_id);
