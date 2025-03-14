create table user (
    id varchar(256) primary key not null,
    email varchar(256) not null,
    password varchar(256) not null,
    password_score double,
    shannon_entropy double
);
