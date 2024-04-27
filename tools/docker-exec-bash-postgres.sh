#!/usr/bin/env bash

cd $(dirname $0) || exit
cd ../docker || exit

echo "+++++++++++++++++++++++++++++++++++++++"
echo "Connecting to PostgreSQL container..."
echo "+++++++++++++++++++++++++++++++++++++++"

TMPDIR=/private$TMPDIR docker exec -it local-postgres bash
