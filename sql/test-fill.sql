INSERT INTO "author"
("id", "name"   , "surname"    , "birth_year", "death_year") VALUES
(1   , 'William', 'Shakespeare', 1564        , 1616        ),
(2   , 'Fenimor', 'Cooper'     , 1789        , 1851        ),
(3   , 'Jack'   , 'London'     , 1876        , 1916        ),
(4   , 'Liu'    , 'Cixin'      , 1963        , NULL        ),
(5   , 'Roger'  , 'Zelazny'    , 1937        , 1995        ),
(6   , 'Robert' , 'Sheckley'   , 1928        , 2005        ),
(7   , 'Harry'  , 'Harrison'   , 1925        , 2012        );
SELECT setval('author_id_seq', 7);

INSERT INTO "book"
("id", "title"                               , "year") VALUES
(1   , 'Hamlet'                              , 1604  ),
(2   , 'King Lear'                           , 1608  ),
(3   , 'Merchant of Venice'                  , 1596  ),
(4   , 'Pathfinder'                          , 1840  ),
(5   , 'Holy Bible'                          , 1450  ),
(6   , 'The Three-Body Problem'              , 2006  ),
(7   , 'The Dark Forest'                     , 2008  ),
(8   , 'Death''s End'                        , 2010  ),
(9   , 'The Dream Master'                    , 1965  ),
(10  , 'Damnation Alley'                     , 1969  ),
(11  , 'Minotaur Maze'                       , 1990  ),
(12  , 'Watchbird'                           , 1990  ),
(13  , 'Bring Me the Head of Prince Charming', 1991  ),
(14  , 'The Stainless Steel Rat'             , 1966  ),
(15  , 'On The Planet of Bottled Brains'     , 1990  );
SELECT setval('book_id_seq', 15);

INSERT INTO "author_vs_book"
("author_id", "book_id") VALUES
(1          , 1        ),
(1          , 2        ),
(1          , 3        ),
(2          , 4        ),
(4          , 6        ),
(4          , 7        ),
(4          , 8        ),
(5          , 9        ),
(5          , 10       ),
(6          , 11       ),
(6          , 12       ),
(5          , 13       ),
(6          , 13       ),
(7          , 14       ),
(6          , 15       ),
(7          , 15       );
