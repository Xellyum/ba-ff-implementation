INSERT IGNORE INTO feature_flag (feature, active, created_at, description, environments)
VALUES ('quality-check-enabled', true, NOW(), 'Quality Check Feature', 'DEVELOPMENT'),
       ('priority-queue-enabled', true, NOW(), 'Priority Queue Feature', 'DEVELOPMENT,PRODUCTION'),
       ('resource-management-enabled', false, NOW(), 'Resource Management Feature', 'DEVELOPMENT,STAGE,PRODUCTION');
