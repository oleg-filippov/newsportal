DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS news_tag;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user_role;

-- -----------------------------------------------------
-- Table USER
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  login VARCHAR(20) NOT NULL,
  password VARCHAR(60) NOT NULL,
  email VARCHAR(30) NOT NULL,
  name VARCHAR(50) NOT NULL,
  registered TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  enabled BOOLEAN DEFAULT TRUE,
  locked BOOLEAN DEFAULT FALSE
);

CREATE PRIMARY KEY ON user (id);
CREATE UNIQUE INDEX login_unique ON user (login ASC);
CREATE UNIQUE INDEX email_unique ON user (email ASC);
ALTER TABLE user ADD CONSTRAINT login_unique UNIQUE (login);
ALTER TABLE user ADD CONSTRAINT email_unique UNIQUE (email);

-- -----------------------------------------------------
-- Table NEWS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS news (
  id BIGINT NOT NULL AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  preview VARCHAR(250) NOT NULL,
  content TEXT NOT NULL,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_modified TIMESTAMP NULL,
  views_count INT DEFAULT 0,
  comments_count INT DEFAULT 0,
  user_id BIGINT NOT NULL
);

CREATE PRIMARY KEY ON news (id);
CREATE INDEX fk_news_user1_idx ON news (user_id ASC);
ALTER TABLE news ADD CONSTRAINT fk_news_user1_idx FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON DELETE RESTRICT
	ON UPDATE RESTRICT;

-- -----------------------------------------------------
-- Table COMMENT
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS comment (
  id BIGINT NOT NULL AUTO_INCREMENT,
  content VARCHAR(500) NOT NULL,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  user_id BIGINT NOT NULL,
  news_id BIGINT NOT NULL
);

CREATE PRIMARY KEY ON comment (id);
CREATE INDEX fk_comment_user1_idx ON comment (user_id ASC);
CREATE INDEX fk_comment_news1_idx ON comment (news_id ASC);
ALTER TABLE comment ADD CONSTRAINT fk_comment_user1_idx FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON DELETE RESTRICT
	ON UPDATE RESTRICT;
ALTER TABLE comment ADD CONSTRAINT fk_comment_news1_idx FOREIGN KEY (news_id)
	REFERENCES news (id)
	ON DELETE CASCADE
	ON UPDATE RESTRICT;

-- -----------------------------------------------------
-- Table TAG
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tag (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL
);

CREATE PRIMARY KEY ON tag (id);
CREATE UNIQUE INDEX name_unique ON tag (name ASC);
ALTER TABLE tag ADD CONSTRAINT name_unique UNIQUE (name);

-- -----------------------------------------------------
-- Table NEWS_TAG
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS news_tag (
  news_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL
);

CREATE PRIMARY KEY ON news_tag (news_id, tag_id);
CREATE INDEX fk_news_tag_news1_idx ON news_tag (news_id ASC);
CREATE INDEX fk_news_tag_tag1_idx ON news_tag (tag_id ASC);
ALTER TABLE news_tag ADD CONSTRAINT fk_news_tag_news1_idx FOREIGN KEY (news_id)
	REFERENCES news (id)
	ON DELETE RESTRICT
	ON UPDATE RESTRICT;
ALTER TABLE news_tag ADD CONSTRAINT fk_news_tag_tag1_idx FOREIGN KEY (tag_id)
	REFERENCES tag (id)
	ON DELETE RESTRICT
	ON UPDATE RESTRICT;

-- -----------------------------------------------------
-- Table ROLE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS role (
  id BIGINT NOT NULL AUTO_INCREMENT,
  authority VARCHAR(20) NOT NULL
);

CREATE PRIMARY KEY ON role (id);
CREATE UNIQUE INDEX authority_unique ON role (authority ASC);
ALTER TABLE role ADD CONSTRAINT authority_unique UNIQUE (authority);

-- -----------------------------------------------------
-- Table USER_ROLE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL
);

CREATE PRIMARY KEY ON user_role (user_id, role_id);
CREATE INDEX fk_user_role_user1_idx ON user_role (user_id ASC);
CREATE INDEX fk_user_role_role1_idx ON user_role (role_id ASC);
ALTER TABLE user_role ADD CONSTRAINT fk_user_role_user1_idx FOREIGN KEY (user_id)
	REFERENCES user (id)
	ON DELETE RESTRICT
	ON UPDATE RESTRICT;
ALTER TABLE user_role ADD CONSTRAINT fk_user_role_role1_idx FOREIGN KEY (role_id)
	REFERENCES role (id)
	ON DELETE RESTRICT
	ON UPDATE RESTRICT;



-- -----------------------------------------------------
-- DUMP DATA
-- -----------------------------------------------------

INSERT INTO user (`login`, `password`, `email`, `name`) VALUES
('admin', '$2a$10$MXqqS2vMDy9DAAB53udQ5Oc27OHoCXvoV.MtJZg7Lrg7kUDqxU/L.', 'admin@gmail.com', 'Admin'),
('author', '$2a$10$RewEvCU1BRWPJPLwojKbZODmilxRuRDoVd5DmcqjT9QUrXfmgtefy', 'author@gmail.com', 'Вася'),
('user', '$2a$10$7w5J60.3TQxGJZ0GGEKGEuU6bkDXWET1SGQZ1EfNE6/mdolLMDe0i', 'user@gmail.com', 'Маша');

INSERT INTO role (`authority`) VALUES
('ROLE_ADMIN'),
('ROLE_AUTHOR'),
('ROLE_USER');

INSERT INTO user_role (`user_id`, `role_id`) VALUES
('1', '1'), ('1', '2'), ('1', '3'),
('2', '2'), ('2', '3'),
('3', '3');
