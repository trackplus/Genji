ALTER TABLE TPROJECT ADD LABEL2 VARCHAR(35);
UPDATE TPROJECT SET LABEL2 = LABEL;
ALTER TABLE TPROJECT DROP LABEL;
ALTER TABLE TPROJECT ADD LABEL VARCHAR(35);
UPDATE TPROJECT SET LABEL = LABEL2;
ALTER TABLE TPROJECT DROP LABEL2;

ALTER TABLE TWORKITEM ADD TMPSYNOPSIS VARCHAR(80);
UPDATE TWORKITEM SET TMPSYNOPSIS = PACKAGESYNOPSYS;
ALTER TABLE TWORKITEM DROP PACKAGESYNOPSYS;
ALTER TABLE TWORKITEM ADD PACKAGESYNOPSYS VARCHAR(80);
UPDATE TWORKITEM SET PACKAGESYNOPSYS = TMPSYNOPSIS;
ALTER TABLE TWORKITEM DROP TMPSYNOPSIS;

commit;