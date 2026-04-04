create schema if not exists my_blog;
set search_path to my_blog;

drop table if exists comments;
drop table if exists posts;


create table posts(
    id bigserial primary key,
    title character varying(1000),
    text text,
    tags text[],
    likes_count integer,
    file_name character varying(1000)
);

create table comments(
     id bigserial primary key,
     text text,
     post_id bigint references posts(id)
);

insert into posts(id, title, text, tags, likes_count)
values (1, 'post1', 'text1', array['#tag1', '#tag2'], 0),
        (2, 'post2', 'text2', array['#tag3', '#tag4'], 1);

