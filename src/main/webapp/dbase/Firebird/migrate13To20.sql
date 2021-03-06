/*
 * Project:      Track+
 * Author:       J�rg Friedrich
 *
 * Date Created: Wednesday, October 23, 2002
 *
 *
 * Populates the ID_TABLE to migrate from a previous database release 1.3
 * to database release 2.0
 * 
 */

CREATE TABLE ID_TABLE
(
    "ID_TABLE_ID" INTEGER NOT NULL,
    "TABLE_NAME" VARCHAR (64) NOT NULL,
    "NEXT_ID" INTEGER,
    "QUANTITY" INTEGER,
    PRIMARY KEY(ID_TABLE_ID),
    UNIQUE (TABLE_NAME)
);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(1, 'TACL', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(2, 'TATTACHMENT', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(3, 'TBASELINE', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(4, 'TCATEGORY', 8, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(5, 'TCLASS', 4, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(6, 'TDATES', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(7, 'TDEPARTMENT', 11, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(8, 'TNOTIFY', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(9, 'TPERSON', 100, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(10, 'TPLAN', 1, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(11, 'TPRIORITY', 4, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(12, 'TPROJCAT', 10, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(13, 'TPROJECT', 10, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(14, 'TRELEASE', 4, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(15, 'TROLE', 8, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(16, 'TSEVERITY', 4, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(17, 'TSTATE', 10, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(18, 'TSTATECHANGE', 10, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(19, 'TTRAIL', 10, 1);

INSERT INTO ID_TABLE (ID_TABLE_ID, TABLE_NAME, NEXT_ID, QUANTITY) VALUES
			(20, 'TWORKITEM', 10, 1);

commit;


UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TBASELINE.BLKEY) FROM TBASELINE) + 1 
	WHERE TABLE_NAME='TBASELINE';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TCATEGORY.PKEY) FROM TCATEGORY) + 1 
	WHERE TABLE_NAME='TCATEGORY';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TCLASS.PKEY) FROM TCLASS) + 1 
	WHERE TABLE_NAME='TCLASS';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TCLASS.PKEY) FROM TCLASS) + 1 
	WHERE TABLE_NAME='TCLASS';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TDEPARTMENT.PKEY) FROM TDEPARTMENT) + 1 
	WHERE TABLE_NAME='TDEPARTMENT';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TNOTIFY.PKEY) FROM TNOTIFY) + 1 
	WHERE TABLE_NAME='TNOTIFY';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TPERSON.PKEY) FROM TPERSON) + 1 
	WHERE TABLE_NAME='TPERSON';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TPRIORITY.PKEY) FROM TPRIORITY) + 1 
	WHERE TABLE_NAME='TPRIORITY';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TPROJCAT.PKEY) FROM TPROJCAT) + 1 
	WHERE TABLE_NAME='TPROJCAT';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TPROJECT.PKEY) FROM TPROJECT) + 1 
	WHERE TABLE_NAME='TPROJECT';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TROLE.PKEY) FROM TROLE) + 1 
	WHERE TABLE_NAME='TROLE';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TSEVERITY.PKEY) FROM TSEVERITY) + 1 
	WHERE TABLE_NAME='TSEVERITY';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TSTATE.PKEY) FROM TSTATE) + 1 
	WHERE TABLE_NAME='TSTATE';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TSTATE.PKEY) FROM TSTATE) + 1 
	WHERE TABLE_NAME='TSTATE';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TSTATECHANGE.STATECHANGEKEY) FROM TSTATECHANGE) + 1 
	WHERE TABLE_NAME='TSTATECHANGE';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TTRAIL.TRAILKEY) FROM TTRAIL) + 1 
	WHERE TABLE_NAME='TTRAIL';

UPDATE ID_TABLE SET NEXT_ID = 
	(SELECT MAX(TWORKITEM.WORKITEMKEY) FROM TWORKITEM) + 1 
	WHERE TABLE_NAME='TWORKITEM';

commit;
