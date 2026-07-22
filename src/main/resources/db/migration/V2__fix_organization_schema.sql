UPDATE organization
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

UPDATE organization
SET updated_at = CURRENT_TIMESTAMP
WHERE updated_at IS NULL;

ALTER TABLE organization
ALTER COLUMN email TYPE VARCHAR(255);

ALTER TABLE organization
    ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE organization
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE organization
    ALTER COLUMN active SET DEFAULT TRUE;