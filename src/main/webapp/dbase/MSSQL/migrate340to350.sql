INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(88, 'TCOMPUTEDVALUES', 1, 1);
INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES(89, 'TLINKTYPE', 1, 1);

ALTER TABLE TREPORTLAYOUT DROP CONSTRAINT TREPORTLAYOUT_FK_4;
ALTER TABLE TREPORTLAYOUT ADD FIELDTYPE INTEGER;

/* ---------------------------------------------------------------------- */
/* TCOMPUTEDVALUES                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOMPUTEDVALUES_FK_1')
    ALTER TABLE TCOMPUTEDVALUES DROP CONSTRAINT TCOMPUTEDVALUES_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TCOMPUTEDVALUES_FK_2')
    ALTER TABLE TCOMPUTEDVALUES DROP CONSTRAINT TCOMPUTEDVALUES_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TCOMPUTEDVALUES')
BEGIN
     DECLARE @reftable_20 nvarchar(60), @constraintname_20 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TCOMPUTEDVALUES'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_20, @constraintname_20
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_20+' drop constraint '+@constraintname_20)
       FETCH NEXT from refcursor into @reftable_20, @constraintname_20
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TCOMPUTEDVALUES
END
;

CREATE TABLE TCOMPUTEDVALUES
(
    PKEY INT NOT NULL,
    WORKITEMKEY INT NOT NULL,
    EFFORTTYPE INT NOT NULL,
    COMPUTEDVALUETYPE INT NOT NULL,
    COMPUTEDVALUE FLOAT NULL,
    MEASUREMENTUNIT INT NULL,
    PERSON INT NULL,

    CONSTRAINT TCOMPUTEDVALUES_PK PRIMARY KEY(PKEY));

	
BEGIN
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_1 FOREIGN KEY (WORKITEMKEY)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;

BEGIN
ALTER TABLE TCOMPUTEDVALUES
    ADD CONSTRAINT TCOMPUTEDVALUES_FK_2 FOREIGN KEY (PERSON)
    REFERENCES TPERSON (PKEY)
END
;


IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLOCALIZEDRESOURCES')
BEGIN
     DECLARE @reftable_82 nvarchar(60), @constraintname_82 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLOCALIZEDRESOURCES'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_82, @constraintname_82
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_82+' drop constraint '+@constraintname_82)
       FETCH NEXT from refcursor into @reftable_82, @constraintname_82
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLOCALIZEDRESOURCES
END
;

CREATE TABLE TLOCALIZEDRESOURCES
(
    OBJECTID INT NOT NULL,
    TABLENAME VARCHAR (255) NULL,
    PRIMARYKEYVALUE INT NULL,
    FIELDNAME VARCHAR (255) NOT NULL,
    LOCALIZEDTEXT VARCHAR (255) NULL,
    LOCALE VARCHAR (20) NULL,

    CONSTRAINT TLOCALIZEDRESOURCES_PK PRIMARY KEY(OBJECTID));
	
	
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TLINKTYPE')
BEGIN
     DECLARE @reftable_83 nvarchar(60), @constraintname_83 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TLINKTYPE'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_83, @constraintname_83
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_83+' drop constraint '+@constraintname_83)
       FETCH NEXT from refcursor into @reftable_83, @constraintname_83
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TLINKTYPE
END
;

CREATE TABLE TLINKTYPE
(
    OBJECTID INT NOT NULL,
    NAME VARCHAR (255) NOT NULL,
    MOREPROPS VARCHAR (3000) NULL,

    CONSTRAINT TLINKTYPE_PK PRIMARY KEY(OBJECTID));

	
BEGIN
ALTER TABLE TWORKITEMLINK
    ADD CONSTRAINT TWORKITEMLINK_FK_3 FOREIGN KEY (LINKTYPE)
    REFERENCES TLINKTYPE (OBJECTID)
END
;

update TSTATE set symbol='opened.gif' where pkey=1;
update TSTATE set symbol='analyzed.gif' where pkey=2;
update TSTATE set symbol='assigned.gif' where pkey=3;
update TSTATE set symbol='suspended.gif' where pkey=4;
update TSTATE set symbol='processing.gif' where pkey=5;
update TSTATE set symbol='implemented.gif' where pkey=6;
update TSTATE set symbol='integrated.gif' where pkey=7;
update TSTATE set symbol='closed.gif' where pkey=8;
update TSTATE set symbol='assessing.gif' where pkey=9;

update TPRIORITY set symbol='occasionally.gif' where pkey=1;
update TPRIORITY set symbol='soon.gif' where pkey=2;
update TPRIORITY set symbol='immediate.gif' where pkey=3;

update TSEVERITY set symbol='minor.gif' where pkey=1;
update TSEVERITY set symbol='major.gif' where pkey=2;
update TSEVERITY set symbol='blocker.gif' where pkey=3;
