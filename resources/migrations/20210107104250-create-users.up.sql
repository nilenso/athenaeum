CREATE TABLE IF NOT EXISTS users(
    id bigserial primary key,
    google_id text not null,
    name text not null,
    email text not null,
    photo_url text
);
