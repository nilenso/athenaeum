#!/bin/bash

psql postgres <<EOPSQL
CREATE USER athenaeum_staging_user WITH PASSWORD 'athenaeum_staging_pwd';
CREATE DATABASE athenaeum_staging;
GRANT ALL PRIVILEGES ON DATABASE athenaeum_staging to athenaeum_staging_user;
EOPSQL
