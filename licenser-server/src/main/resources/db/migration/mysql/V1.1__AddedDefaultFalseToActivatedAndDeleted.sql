UPDATE `access_code`
SET `is_activated` = FALSE
WHERE `is_activated` IS NULL;


UPDATE `access_code`
SET `deleted` = FALSE
WHERE `deleted` IS NULL;


ALTER TABLE `access_code`
  MODIFY `is_activated`  BIT NOT NULL DEFAULT FALSE,
  MODIFY `deleted` BIT NOT NULL DEFAULT FALSE;
