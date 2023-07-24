#  faking stock: here default value after migration should be 0
ALTER TABLE product
    ADD COLUMN quantity_on_hand integer default 0;

# We add default stock where value is null
UPDATE product
    SET product.quantity_on_hand = 10 WHERE product.quantity_on_hand IS NULL;