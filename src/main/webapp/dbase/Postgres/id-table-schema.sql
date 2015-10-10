
-----------------------------------------------------------------------------
-- ID_TABLE
-----------------------------------------------------------------------------
--DROP TABLE ID_TABLE CASCADE;



CREATE TABLE ID_TABLE
(
    ID_TABLE_ID INTEGER NOT NULL,
    TABLE_NAME VARCHAR(128) NOT NULL,
    NEXT_ID INTEGER,
    QUANTITY INTEGER,
    PRIMARY KEY (ID_TABLE_ID),
    CONSTRAINT ID_TABLE_U_1 UNIQUE (TABLE_NAME)
);




----------------------------------------------------------------------
-- ID_TABLE
----------------------------------------------------------------------


