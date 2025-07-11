drop database  quiz_website;
CREATE DATABASE IF NOT EXISTS quiz_website;
USE quiz_website;

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       salt VARCHAR(255),
                       is_admin BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friendships (
                             requester_id BIGINT,
                             receiver_id BIGINT,
                             status ENUM('pending', 'accepted') DEFAULT 'pending',
                             requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (requester_id) REFERENCES users(user_id) ON DELETE CASCADE,
                             FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
                             PRIMARY KEY(requester_id,receiver_id)
);

CREATE TABLE quizzes (
                         quiz_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(100) NOT NULL,
                         description TEXT,
                         creator_id BIGINT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (creator_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE messages (
                          message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sender_id BIGINT,
                          receiver_id BIGINT,
                          type ENUM('note', 'challenge') NOT NULL,
                          content TEXT,
                          quiz_id BIGINT,
                          sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
                          FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
                          FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE SET NULL
);

CREATE TABLE questions (
                           question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           quiz_id BIGINT,
                           question_text TEXT,
                           type ENUM('question_response', 'fill_blank', 'multiple_choice', 'picture_response') NOT NULL,
                           image_url TEXT,
                           question_order INT,
                           FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

CREATE TABLE choices (
                         choice_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         question_id BIGINT,
                         choice_text VARCHAR(255),
                         is_correct BOOLEAN DEFAULT FALSE,
                         FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

CREATE TABLE question_answers (
                                  answer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  question_id BIGINT,
                                  answer_text VARCHAR(255),
                                  FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);

CREATE TABLE quiz_attempts (
                               attempt_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               quiz_id BIGINT,
                               user_id BIGINT,
                               score FLOAT,
                               total_questions INT,
                               time_taken INT,
                               attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE,
                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE responses (
                           response_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           attempt_id BIGINT,
                           question_id BIGINT,
                           response_text TEXT,
                           FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(attempt_id) ON DELETE CASCADE,
                           FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);




