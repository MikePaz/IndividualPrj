CREATE DATABASE messages;

CREATE TABLE user(
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(32) NOT NULL ,
  password varchar(32) NOT NULL ,
  role    INT NOT NULL ,
  primary key (id));


create table message (
  id INT NOT NULL AUTO_INCREMENT,
  date DATETIME NOT NULL,
  sender INT NOT NULL,
  receiver INT NOT NULL,
  text TEXT NOT NULL,
  PRIMARY KEY  (id) ,
  INDEX sender (sender ASC));