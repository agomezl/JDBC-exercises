#!/bin/bash

set -e

PGPASSWORD=secret
POSTGRES_USER=tda357
POSTGRES_HOST=localhost
POSTGRES_HOST_PORT=5432
POSTGRES_DB=computer_store

export PGPASSWORD

PSQL_CMD="psql -U ${POSTGRES_USER} -h ${POSTGRES_HOST} -p ${POSTGRES_HOST_PORT}"

${PSQL_CMD} <<EOF
CREATE DATABASE ${POSTGRES_DB};
EOF

${PSQL_CMD} -d ${POSTGRES_DB} -f computer-shop.sql
