CREATE DATABASE users;
CREATE TABLE users(
  idUS INT  PRIMARY KEY,
  HoTen VARCHAR(255) NOT NULL,
  Username VARCHAR(255) NOT NULL,
  Password INT  not null,
  email VARCHAR(200) NOT NULL,
  Phone VARCHAR(15) NOT NULL
);
INSERT INTO users (idUS, HoTen, Username, Password, email, Phone) VALUES ('', 'lethe', 'lethe', '123456', 'lethe@gmail.com', '09051234566');
