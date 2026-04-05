create schema if not exists my_blog;
set search_path to my_blog;

drop table if exists comments;
drop table if exists posts;


create table posts(
    id bigserial primary key,
    title character varying(1000),
    text text,
    tags text[],
    likes_count integer default 0,
    file_name character varying(1000)
);

create table comments(
     id bigserial primary key,
     text text,
     post_id bigint references posts(id)
);

