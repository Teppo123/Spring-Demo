DROP TABLE IF EXISTS billionaires;

CREATE TABLE IF NOT EXISTS users (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250) NOT NULL,
	birth_date DATE NOT NULL,
	deactivated BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP NOT NULL
);
  
INSERT INTO users (first_name, last_name, birth_date, created_at, deactivated) VALUES
	('Matti', 'Meikäläinen', '1980-01-01', '2020-01-01 00:00:00', FALSE),
	('Jaakko', 'Jaakkolainen', '1973-04-10', '2020-01-01 00:01:00', FALSE),
	('Teppo', 'Teppolainen', '2001-11-12', '2020-01-01 00:02:00', FALSE),
	('Poistettu', 'Poistettulainen', '1980-01-01', '2020-01-01 00:03:00', TRUE);