# initially, not autoincrement:  id bigint not null
alter table book CHANGE id id BIGINT auto_increment;
alter table author CHANGE id id BIGINT auto_increment;
drop table if exists book_seq;
drop table if exists author_seq;

