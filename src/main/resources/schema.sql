create schema if not exists my_blog;
set search_path to my_blog;

drop table if exists posts_tags;
drop table if exists comments;
drop table if exists tags;
drop table if exists posts;


create table posts(
    id bigserial primary key,
    title character varying(1000),
    text text,
    likes_count integer,
    file_name character varying(1000)
);

create table tags(
    id bigserial primary key,
    text text
);

create table comments(
     id bigserial primary key,
     text text,
     post_id bigint references posts(id)
);

create table posts_tags(
    id bigserial primary key,
    post_id bigint references posts(id),
    tag_id bigint references tags(id)
)


