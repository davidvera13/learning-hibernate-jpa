UPDATE order_header
SET version = 0 WHERE version IS NULL;

update order_line
SET version = 0 WHERE version IS NULL;