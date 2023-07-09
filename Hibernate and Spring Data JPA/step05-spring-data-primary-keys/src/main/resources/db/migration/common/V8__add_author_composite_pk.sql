drop table if exists author_composite;

create table author_composite
(
    first_name varchar(255),
    last_name varchar(255),
    country varchar(255),
    author_id bigint,
    primary key (first_name, last_name)
) engine = InnoDB;