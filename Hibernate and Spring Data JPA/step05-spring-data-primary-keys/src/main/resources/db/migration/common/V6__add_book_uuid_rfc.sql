drop table if exists book_uuid_rfc;

create table book_uuid_rfc
(
    id         binary(16),
    isbn varchar(255),
    publisher varchar(255),
    title varchar(255),
    author_id bigint,
    primary key (id)
) engine = InnoDB;