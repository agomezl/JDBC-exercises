#!/bin/bash

POSTGRES_NAME=testDB
POSTGRES_HOST_PORT=5432

# Re-launch the postgres docker
docker kill $POSTGRES_NAME &> /dev/null
docker rm   $POSTGRES_NAME &> /dev/null

POSTGRES_CREDENTIALS="-e POSTGRES_USER=tda357 -e POSTGRES_DB=tda357 -e POSTGRES_PASSWORD=secret"

if ! docker ps | grep ${POSTGRES_NAME} &> /dev/null; then
    docker run -d ${POSTGRES_CREDENTIALS} \
           -p ${POSTGRES_HOST_PORT}:5432 \
           --name ${POSTGRES_NAME} \
           postgres:9.2.14
    docker start ${POSTGRES_NAME}

    # Wait for the postgres server
    PSQL_CMD="psql -U tda357 -c \\l"
    DB_READY=false
    for TRIES in {1..20}
    do export PGPASSWORD=secret
       # Can we connect to any of this servers?
       # Here we try both localhost and postgres-server
       if (${PSQL_CMD} -h localhost -p ${POSTGRES_HOST_PORT}) &> /dev/null
       then DB_READY=true
            break
            # If not, just wait a second
       else sleep 1
       fi
    done

    # Did we got a successful connection?
    if ! ${DB_READY}
    then
        echo "Database is not ready after 20 tries"
        exit 1
    else
        echo "DB is ready"
    fi
fi
