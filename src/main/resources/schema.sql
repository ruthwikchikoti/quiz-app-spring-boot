CREATE TABLE IF NOT EXISTS questions (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         question_text VARCHAR(500) NOT NULL,
    optiona VARCHAR(255) NOT NULL,
    optionb VARCHAR(255) NOT NULL,
    optionc VARCHAR(255) NOT NULL,
    optiond VARCHAR(255) NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,
    time_limit INT DEFAULT 20 NOT NULL
    );

CREATE TABLE IF NOT EXISTS quiz_sessions (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             start_time TIMESTAMP NOT NULL,
                                             last_access_time TIMESTAMP NOT NULL,
                                             active BOOLEAN NOT NULL DEFAULT TRUE,
                                             total_questions INT NOT NULL DEFAULT 0,
                                             correct_answers INT NOT NULL DEFAULT 0,
                                             current_question_id BIGINT
);

CREATE TABLE IF NOT EXISTS user_responses (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              session_id BIGINT NOT NULL,
                                              question_id BIGINT NOT NULL,
                                              user_answer VARCHAR(255) NOT NULL,
    correct BOOLEAN NOT NULL,
    answered_at TIMESTAMP NOT NULL,
    response_time INT,
    FOREIGN KEY (session_id) REFERENCES quiz_sessions(id),
    FOREIGN KEY (question_id) REFERENCES questions(id)
    );