
/* ---------------------------------------------------------------------- */
/* ID_TABLE                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'ID_TABLE')
BEGIN
     DECLARE @reftable_1 nvarchar(60), @constraintname_1 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'ID_TABLE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_1, @constraintname_1
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_1+' drop constraint '+@constraintname_1)
       FETCH NEXT from refcursor into @reftable_1, @constraintname_1
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE ID_TABLE
END
;

CREATE TABLE ID_TABLE
(
    ID_TABLE_ID INT NOT NULL,
    TABLE_NAME VARCHAR (128) NOT NULL,
    NEXT_ID INT NULL,
    QUANTITY INT NULL,

    CONSTRAINT ID_TABLE_PK PRIMARY KEY(ID_TABLE_ID),
    UNIQUE (TABLE_NAME));





/* ---------------------------------------------------------------------- */
/* ID_TABLE                                                      */
/* ---------------------------------------------------------------------- */



