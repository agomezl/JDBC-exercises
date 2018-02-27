#!/bin/bash

set -e

PGPASSWORD=secret
POSTGRES_USER=tda357
POSTGRES_HOST=localhost
POSTGRES_HOST_PORT=5432
POSTGRES_DB=songs

export PGPASSWORD

PSQL_CMD="psql -U ${POSTGRES_USER} -h ${POSTGRES_HOST} -p ${POSTGRES_HOST_PORT}"

${PSQL_CMD} <<EOF
CREATE DATABASE ${POSTGRES_DB};
\c ${POSTGRES_DB};
CREATE TABLE Song (theme TEXT
                      , Title TEXT
                      , Artist TEXT
                      , Year INTEGER CHECK (year > 1900 AND year < 2020)
                      , Url TEXT
                      , PRIMARY KEY (Title,Artist,Theme));
\COPY Song FROM './Songs.csv' WITH (FORMAT csv);
EOF
