#!/bin/bash
set -e

# The 'psql' command is available because this script runs inside the postgres container.
# It executes the SQL command passed with the -c flag.
# The -v ON_ERROR_STOP=1 ensures that the script will exit immediately if a command fails.
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Create database for the authentication service
    CREATE DATABASE auth_service;

    -- Create database for the order service
    CREATE DATABASE order_service;
EOSQL
