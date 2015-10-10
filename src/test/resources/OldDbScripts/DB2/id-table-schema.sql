
-----------------------------------------------------------------------------
-- ID_TABLE
-----------------------------------------------------------------------------
-- drop table ID_TABLE;

CREATE TABLE ID_TABLE
(
    ID_TABLE_ID INTEGER NOT NULL,
    TABLE_NAME VARCHAR (128) NOT NULL,
    NEXT_ID INTEGER,
    QUANTITY INTEGER,
    UNIQUE (TABLE_NAME)
);

ALTER TABLE ID_TABLE
    ADD PRIMARY KEY (ID_TABLE_ID);







-----------------------------------------------------------------------------
-- ID_TABLE: FOREIGN KEYS
-----------------------------------------------------------------------------

