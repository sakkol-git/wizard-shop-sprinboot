alter table users
    modify password varchar(255) null;

alter table users
    add provider varchar(20) default 'LOCAL' not null;

alter table users
    add provider_id varchar(255) null;