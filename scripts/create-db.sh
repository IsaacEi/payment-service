#!/usr/bin/env bash
set -euo pipefail

# Carga variables desde .env si existe.
if [ -f .env ]; then
  set -a
  source .env
  set +a
fi

DATABASE_NAME="${DATABASE_NAME:-paymentsdb}"
DATABASE_USER="${DATABASE_USER:-sa}"
MSSQL_SA_PASSWORD="${MSSQL_SA_PASSWORD:-YourStrong!Passw0rd}"
SQLSERVER_HOST="${SQLSERVER_HOST:-localhost}"
SQLSERVER_PORT="${SQLSERVER_PORT:-1433}"

sqlcmd -S "${SQLSERVER_HOST},${SQLSERVER_PORT}" \
  -U "${DATABASE_USER}" \
  -P "${MSSQL_SA_PASSWORD}" \
  -C \
  -v DATABASE_NAME="${DATABASE_NAME}" \
  -i ./scripts/init-db.sql
