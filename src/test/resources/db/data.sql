INSERT INTO my_blog.posts (title, text, tags, likes_count, file_name)
VALUES ('Первый пост', 'Текст поста', ARRAY ['tag1','tag2'], 5, null),
       ('Второй пост', 'Текст второго поста', ARRAY ['tag3','tag2'], 12, null);

INSERT INTO my_blog.comments (text, post_id)
VALUES ('Отличный пост!', 1),
       ('Согласен!', 1),
       ('Интересно', 2);

commit;