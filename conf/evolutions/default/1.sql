# --- !Ups

CREATE TABLE aged
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name TINYTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    kana TINYTEXT CHARACTER SET utf8 COLATTE utf8_general_ci NOT NULL,
    age TINYINT UNSIGNED NOT NULL,
    sex TINYTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    birthed TIMESTAMP NOT NULL,
    address TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    postal TINYTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    phone TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    insuranceId INT UNSIGNED NOT NULL,
    homeId INT UNSIGNED NOT NULL,
    left TIMESTAMP,
    CONSTRAINT insuranceFK FOREIGN KEY (insuranceId) REFERENCES insurances (id),
    CONSTRAINT homeFK FOREIGN KEY (homeId) REFERENCES homes (id),
    INDEX agedIndex (id)
);

# --- !Downs
DROP TABLE aged;
