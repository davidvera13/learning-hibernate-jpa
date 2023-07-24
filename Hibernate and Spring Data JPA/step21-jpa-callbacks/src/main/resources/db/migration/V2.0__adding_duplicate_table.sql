CREATE TABLE credit_card_convert (
                             id bigint not null auto_increment,
                             credit_card_number varchar(20),
                             cvv varchar(4),
                             expiration_date varchar(7),
                             PRIMARY KEY (id)
);