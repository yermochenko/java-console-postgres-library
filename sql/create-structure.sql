CREATE TABLE "author" (
    "id"                  SERIAL   PRIMARY KEY,
    "surname"             TEXT     NOT NULL,
    "name"                TEXT     NOT NULL,
    "birth_year"          INTEGER  NOT NULL DEFAULT 1900,
    "death_year"          INTEGER
);

CREATE TABLE "book" (
    "id"                  SERIAL   PRIMARY KEY,
    "title"               TEXT     NOT NULL,
    "year"                INTEGER  NOT NULL
);

/* many-to-many reference example */
CREATE TABLE "author_vs_book" (
    "author_id"           INTEGER  NOT NULL REFERENCES "author" ON UPDATE RESTRICT ON DELETE RESTRICT,
    "book_id"             INTEGER  NOT NULL REFERENCES "book"   ON UPDATE RESTRICT ON DELETE CASCADE,
    PRIMARY KEY("author_id", "book_id") /* example of multiple columns primary key, but it will be better to use only
                                           single column primary key */
);
