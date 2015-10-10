ALTER TABLE TMAILTEMPLATEDEF DROP COLUMN MAILBODY;
ALTER TABLE TMAILTEMPLATEDEF ADD MAILBODY TEXT;
UPDATE TWORKITEM set SUPERIORWORKITEM=null where WORKITEMKEY in
(SELECT a.WORKITEMKEY FROM TWORKITEM a left join TWORKITEM b on a.SUPERIORWORKITEM=b.WORKITEMKEY where a.SUPERIORWORKITEM is not null and b.WORKITEMKEY is null);
BEGIN
ALTER TABLE TWORKITEM
    ADD CONSTRAINT TWORKITEM_FK_14 FOREIGN KEY (SUPERIORWORKITEM)
    REFERENCES TWORKITEM (WORKITEMKEY)
END
;