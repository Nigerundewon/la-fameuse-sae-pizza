DROP TABLE IF EXISTS ingredient CASCADE;

CREATE TABLE ingredient(
    id SERIAL,
    name TEXT,
    price INTEGER,
    CONSTRAINT pk_ingredient PRIMARY KEY(id)
);

INSERT INTO ingredient (name, price) VALUES ('Poivrons',0);
INSERT INTO ingredient (name, price) VALUES ('Chorizo',0);
INSERT INTO ingredient (name, price) VALUES ('Lardons',0);