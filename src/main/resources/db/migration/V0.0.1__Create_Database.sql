CREATE TABLE block_tracker
(
    id    INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    x     INT                            NOT NULL,
    y     INT                            NOT NULL,
    z     INT                            NOT NULL,
    world VARCHAR(64)                    NOT NULL
);