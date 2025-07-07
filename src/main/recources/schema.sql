CREATE DATABASE IF NOT EXISTS quiz_website;
USE quiz_website;


CREATE TABLE Users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       salt VARCHAR(255),
                       is_admin BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Friendships (
                             request_id INT AUTO_INCREMENT PRIMARY KEY,
                             requester_id INT,
                             receiver_id INT,
                             status ENUM('pending', 'accepted') DEFAULT 'pending',
                             requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (requester_id) REFERENCES Users(user_id),
                             FOREIGN KEY (receiver_id) REFERENCES Users(user_id)
);


CREATE TABLE Messages (
                          message_id INT AUTO_INCREMENT PRIMARY KEY,
                          sender_id INT,
                          receiver_id INT,
                          type ENUM('note', 'friend_request', 'challenge') NOT NULL,
                          content TEXT,
                          quiz_id INT,
                          sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (sender_id) REFERENCES Users(user_id),
                          FOREIGN KEY (receiver_id) REFERENCES Users(user_id),
                          FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id)
);


CREATE TABLE Quizzes (
                         quiz_id INT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(100) NOT NULL,
                         description TEXT,
                         creator_id INT,
                         randomize BOOLEAN DEFAULT FALSE,
                         is_one_page BOOLEAN DEFAULT TRUE,
                         immediate_correction BOOLEAN DEFAULT FALSE,
                         practice_mode BOOLEAN DEFAULT FALSE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (creator_id) REFERENCES Users(user_id)
);


CREATE TABLE Questions (
                           question_id INT AUTO_INCREMENT PRIMARY KEY,
                           quiz_id INT,
                           question_text TEXT,
                           type ENUM('question_response', 'fill_blank', 'multiple_choice', 'picture_response') NOT NULL,
                           image_url TEXT,
                           question_order INT,
                           FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id)
);


CREATE TABLE Choices (
                         choice_id INT AUTO_INCREMENT PRIMARY KEY,
                         question_id INT,
                         choice_text VARCHAR(255),
                         is_correct BOOLEAN DEFAULT FALSE,
                         FOREIGN KEY (question_id) REFERENCES Questions(question_id)
);


CREATE TABLE QuestionAnswers (
                                 answer_id INT AUTO_INCREMENT PRIMARY KEY,
                                 question_id INT,
                                 answer_text VARCHAR(255),
                                 answer_order INT DEFAULT 1,
                                 FOREIGN KEY (question_id) REFERENCES Questions(question_id)
);


CREATE TABLE QuizAttempts (
                              attempt_id INT AUTO_INCREMENT PRIMARY KEY,
                              quiz_id INT,
                              user_id INT,
                              score FLOAT,
                              total_questions INT,
                              time_taken INT,
                              is_practice BOOLEAN DEFAULT FALSE,
                              attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id),
                              FOREIGN KEY (user_id) REFERENCES Users(user_id)
);


CREATE TABLE Responses (
                           response_id INT AUTO_INCREMENT PRIMARY KEY,
                           attempt_id INT,
                           question_id INT,
                           response_text TEXT,
                           is_correct BOOLEAN,
                           response_order INT DEFAULT 1,
                           FOREIGN KEY (attempt_id) REFERENCES QuizAttempts(attempt_id),
                           FOREIGN KEY (question_id) REFERENCES Questions(question_id)
);


CREATE TABLE Achievements (
                              achievement_id INT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(100),
                              description TEXT,
                              icon_url TEXT
);


CREATE TABLE UserAchievements (
                                  user_id INT,
                                  achievement_id INT,
                                  earned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (user_id, achievement_id),
                                  FOREIGN KEY (user_id) REFERENCES Users(user_id),
                                  FOREIGN KEY (achievement_id) REFERENCES Achievements(achievement_id)
);


CREATE TABLE Announcements (
                               announcement_id INT AUTO_INCREMENT PRIMARY KEY,
                               admin_id INT,
                               title VARCHAR(100),
                               content TEXT,
                               posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (admin_id) REFERENCES Users(user_id)
);


CREATE TABLE Categories (
                            category_id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(50) UNIQUE
);


CREATE TABLE QuizCategories (
                                quiz_id INT,
                                category_id INT,
                                PRIMARY KEY (quiz_id, category_id),
                                FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id),
                                FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);
