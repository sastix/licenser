UPDATE `access_code`
SET `is_activated` = FALSE
WHERE `is_activated` IS NULL;


UPDATE `access_code`
SET `deleted` = FALSE
WHERE `deleted` IS NULL;

ALTER TABLE access_code ALTER COLUMN is_activated SET NOT NULL;
ALTER TABLE access_code ALTER COLUMN is_activated SET DEFAULT FALSE ;
ALTER TABLE access_code ALTER COLUMN deleted SET NOT NULL;
ALTER TABLE access_code ALTER COLUMN deleted SET DEFAULT FALSE ;