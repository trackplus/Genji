--
-- Project:      Track+
--
-- Date Created:  Wednesday, January 22, 2014
--
-- Populates the Track+ database with some useful defaults

-- SET NAMES ISO8859_1

-- projects and releases
INSERT INTO TPROJECT (PKEY, LABEL, DEFOWNER, DEFMANAGER, DEFINITSTATE, PROJECTTYPE, DELETED, STATUS, PREFIX, MOREPROPS) VALUES (1, 'Issue Tracking example', 1, 1, 1, 1000, 'N', 3, 'IT-', 'useRelease=true\nlinking=true\nworkTracking=false\nworkUnit=1\naccountingInherited=false\ndefaultAccount=1');


INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER) VALUES (1, 'Release 1.0', 1, 9, 1);
INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER) VALUES (2, 'Release 2.0', 1, 9, 2);


INSERT INTO TACL (PERSONKEY, ROLEKEY, PROJKEY) VALUES (1, 2, 1);
INSERT INTO TACL (PERSONKEY, ROLEKEY, PROJKEY) VALUES (1, 6, 1);
INSERT INTO TACL (PERSONKEY, ROLEKEY, PROJKEY) VALUES (1, 7, 1);

INSERT INTO TPROJECTACCOUNT (ACCOUNT, PROJECT, TPUUID) VALUES (1, 1, '140');

INSERT INTO TPROJECT (PKEY, LABEL, DEFOWNER, DEFMANAGER, DEFINITSTATE, PROJECTTYPE, DELETED, STATUS, PREFIX, MOREPROPS) VALUES (2, 'Scrum Project Example', 1, 1, 1, 1001, 'N', 3, 'SCR-', 'useRelease=true\nlinking=true\nworkTracking=false\nworkUnit=1\naccountingInherited=false\ndefaultAccount=1');

INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER) VALUES (3, 'Release 1.0', 2, 9, 2);
INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER) VALUES (4, 'Release 2.0', 2, 9, 1);
INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER) VALUES (11, 'Product Backlog', 2, 9, 3);

INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER, PARENT) VALUES (7, 'Sprint CW12', 2, 9, 1, 3);
INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER, PARENT) VALUES (8, 'Sprint CW11', 2, 9, 2, 3);
INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER, PARENT) VALUES (12, 'Release 1.0 Backlog', 2, 9, 3, 3);

INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER, PARENT) VALUES (9, 'Sprint CW17', 2, 9, 1, 4);
INSERT INTO TRELEASE (PKEY, LABEL, PROJKEY, STATUS, SORTORDER, PARENT) VALUES (10, 'Sprint CW16', 2, 9, 2, 4);


INSERT INTO TACL (PERSONKEY, ROLEKEY, PROJKEY) VALUES (1, 2, 2);
INSERT INTO TACL (PERSONKEY, ROLEKEY, PROJKEY) VALUES (1, 6, 2);
INSERT INTO TACL (PERSONKEY, ROLEKEY, PROJKEY) VALUES (1, 7, 2);

INSERT INTO TPROJECTACCOUNT (ACCOUNT, PROJECT, TPUUID) VALUES (2, 2, '150');
