MERGE INTO orders (ORDER_ID, ORDER_ENTRY, STATUS)
VALUES (1, NOW(), 'ACTIVE'),
       (2, NOW(), 'ACTIVE'),
       (3, NOW(), 'ACTIVE');

MERGE INTO item (item_id, order_id, description, type, production_step)
VALUES (1, 1, 'test item 1', 'CRATE', 'ASSEMBLY'),
       (2, 1, 'test item 2', 'CRATE', 'ASSEMBLY'),
       (3, 1, 'test item 3', 'CRATE', 'ASSEMBLY'),
       (4, 1, 'test item 4', 'CRATE', 'ASSEMBLY'),
       (5, 2, 'test item 5', 'CRATE', 'ASSEMBLY'),
       (6, 2, 'test item 6', 'CRATE', 'ASSEMBLY'),
       (7, 3, 'test item 7', 'CRATE', 'ASSEMBLY'),
       (8, 3, 'test item 8', 'CRATE', 'ASSEMBLY');

MERGE INTO resource_inventory (RESOURCE, CURRENT_STOCK, MAX_STOCK, RECENTLY_USED)
    VALUES ('WOOD', 250, 1000, 0),
           ('PAPER', 250, 1000, 0);
