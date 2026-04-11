DELETE FROM my_blog.comments;
DELETE FROM my_blog.posts;

ALTER TABLE my_blog.posts ALTER COLUMN id RESTART WITH 1;
ALTER TABLE my_blog.comments ALTER COLUMN id RESTART WITH 1;