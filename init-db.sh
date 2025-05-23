#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER ${ALL_PRIVILEGES_USER_LOGIN} WITH PASSWORD '${ALL_PRIVILEGES_USER_PASSWORD}';
    CREATE USER ${WEATHER_DB_LOGIN} WITH PASSWORD '${WEATHER_DB_PASSWORD}';
    CREATE USER ${WINDOW_DB_LOGIN} WITH PASSWORD '${WINDOW_DB_PASSWORD}';

    CREATE DATABASE users_db;
    GRANT ALL PRIVILEGES ON DATABASE users_db TO ${ALL_PRIVILEGES_USER_LOGIN};

    CREATE DATABASE weather_db;
    GRANT ALL PRIVILEGES ON DATABASE weather_db TO ${WEATHER_DB_LOGIN};

    CREATE DATABASE windows_db;
    GRANT ALL PRIVILEGES ON DATABASE windows_db TO ${WINDOW_DB_LOGIN};
EOSQL
