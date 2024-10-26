INSERT INTO shift (name, start_time, end_time)
SELECT 'S1', '00:00:00', '08:00:00'
WHERE NOT EXISTS (SELECT 1 FROM shift WHERE name = 'S1');

INSERT INTO shift (name, start_time, end_time)
SELECT 'S2', '08:00:00', '16:00:00'
WHERE NOT EXISTS (SELECT 1 FROM shift WHERE name = 'S2');

INSERT INTO shift (name, start_time, end_time)
SELECT 'S3', '16:00:00', '00:00:00'
WHERE NOT EXISTS (SELECT 1 FROM shift WHERE name = 'S3');
