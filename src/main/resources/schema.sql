CREATE TABLE IF NOT EXISTS questions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    difficulty VARCHAR(50),
    answer TEXT,
    explanation TEXT,
    created_at TIMESTAMP
);


CREATE INDEX IF NOT EXISTS idx_questions_category
ON questions(category);

CREATE INDEX IF NOT EXISTS idx_questions_difficulty
ON questions(difficulty);