CREATE TABLE IF NOT EXISTS books(
    id bigserial primary key,
    title text not null,
    author text not null,
    isbn text,
    year_of_publication smallint
);
